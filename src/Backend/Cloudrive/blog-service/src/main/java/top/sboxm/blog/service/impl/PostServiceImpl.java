package top.sboxm.blog.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.sboxm.blog.config.ViewCountMQConfig;
import top.sboxm.blog.constants.RedisConstants;
import top.sboxm.blog.exceptions.ArticleException;
import top.sboxm.blog.mapper.PostMapper;
import top.sboxm.blog.pojo.dto.ArticleDTO;
import top.sboxm.blog.pojo.dto.CreatePostDTO;
import top.sboxm.blog.pojo.dto.PostListDTO;
import top.sboxm.blog.pojo.po.Post;
import top.sboxm.blog.pojo.vo.PostCountVO;
import top.sboxm.blog.pojo.vo.PostDetailVO;
import top.sboxm.blog.pojo.vo.PostListVO;
import top.sboxm.blog.pojo.vo.UserPostListVO;
import top.sboxm.blog.remote.EdgeClient;
import top.sboxm.blog.service.ArticleSearchIndexService;
import top.sboxm.blog.service.PostService;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.utils.SecurityContextUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 文章服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    private final EdgeClient edgeClient;
    private final ArticleSearchIndexService articleSearchIndexlService;

    @Override
    public IPage<PostListVO> pageList(PostListDTO dto) {
        // 参数校验
        if (dto == null || dto.getPageNum() == null || dto.getPageSize() == null) {
            throw new ArticleException(RestResultEnum.INVALID_PARAM);
        }

        try {
            // 创建分页对象
            Page<Post> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            
            // 构建查询条件
            LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<Post>()
                    .eq(Post::getDelFlag, 0)
                    .eq(Post::getStatus, 1)
                    .eq(dto.getCategoryId() != 0, Post::getCategoryId, dto.getCategoryId())
                    .orderByDesc(Post::getCreateTime);

            // 执行分页查询
            IPage<Post> postPage = this.page(page, wrapper);

            // 转换为VO对象
            return postPage.convert(post -> {
                PostListVO vo = new PostListVO();
                BeanUtils.copyProperties(post, vo);
                vo.setId(String.valueOf(post.getId()));
                return vo;
            });
        } catch (Exception e) {
            throw new ArticleException(RestResultEnum.OPERATION_FAILED);
        }
    }

    @Override
    public Post createPost(CreatePostDTO dto) {
        // 参数校验
        if (dto == null || dto.getTitle() == null || dto.getContent() == null) {
            throw new ArticleException(RestResultEnum.INVALID_PARAM);
        }

        Post post = new Post();
        BeanUtils.copyProperties(dto, post);

        post.setId(IdUtil.getSnowflake().nextId());
        post.setStatus(0);
        post.setDelFlag(0);
        post.setViewCount(0L);
        post.setCommentCount(0L);
        post.setTopTag("normal");
        
        try {
            // 保存文章
            this.save(post);

            // 提交审核
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setArticleId(String.valueOf(post.getId()));
            edgeClient.submitArticleAudit(articleDTO);

            return post;
        } catch (Exception e) {
            throw new ArticleException(RestResultEnum.OPERATION_FAILED);
        }
    }

    @Override
    public List<UserPostListVO> getUserPostList() {
        try {
            // 获取当前用户ID
            String userId = SecurityContextUtil.getUserId();
            if (userId == null) {
                throw new ArticleException(RestResultEnum.NEED_LOGIN);
            }

            LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<Post>()
                    .eq(Post::getCreateBy, Long.parseLong(userId))
                    .eq(Post::getDelFlag, 0)
                    .orderByDesc(Post::getCreateTime);

            // 查询文章列表
            List<Post> posts = this.list(wrapper);
            return posts.stream().map(post -> {
                UserPostListVO vo = new UserPostListVO();
                BeanUtils.copyProperties(post, vo);
                vo.setId(String.valueOf(post.getId()));
                return vo;
            }).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new ArticleException(RestResultEnum.INVALID_PARAM);
        } catch (Exception e) {
            throw new ArticleException(RestResultEnum.OPERATION_FAILED);
        }
    }

    private final StringRedisTemplate stringRedisTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public PostDetailVO getPostDetail(String id) {
        // 查询文章
        Post post = this.getOne(new LambdaQueryWrapper<Post>()
                .eq(Post::getId, Long.parseLong(id))
                .eq(Post::getDelFlag, 0));

        if (post == null) {
            throw new ArticleException(RestResultEnum.ARTICLE_NOT_FOUND);
        }

        // 处理阅读计数
        String viewCountKey = RedisConstants.ARTICLE_VIEW_COUNT_PREFIX + id;
        String cachedCount = stringRedisTemplate.opsForValue().get(viewCountKey);

        if (cachedCount == null) {
            stringRedisTemplate.opsForValue().set(viewCountKey, String.valueOf(post.getViewCount()), 24, TimeUnit.HOURS);
            cachedCount = String.valueOf(post.getViewCount());
        }

        // 增加阅读计数
        Long newCount = stringRedisTemplate.opsForValue().increment(viewCountKey);
        rabbitTemplate.convertAndSend(ViewCountMQConfig.VIEW_COUNT_EXCHANGE, ViewCountMQConfig.VIEW_COUNT_ROUTING_KEY, id);

        PostDetailVO vo = new PostDetailVO();
        BeanUtils.copyProperties(post, vo);
        vo.setId(String.valueOf(post.getId()));
        vo.setViewCount(newCount);
        return vo;
    }

    @Override
    public void deletePost(String id) {
        // 查询文章
        Post post = this.getOne(new LambdaQueryWrapper<Post>()
                .eq(Post::getId, Long.parseLong(id))
                .eq(Post::getDelFlag, 0));

        if (post == null) {
            throw new ArticleException(RestResultEnum.ARTICLE_NOT_FOUND);
        }

        // 获取当前用户ID
        String userId = SecurityContextUtil.getUserId();
        if (!String.valueOf(post.getCreateBy()).equals(userId)) {
            throw new ArticleException(RestResultEnum.NO_PERMISSION_ARTICLE);
        }

        boolean result = this.removeById(post.getId());
        if (!result) {
            throw new ArticleException(RestResultEnum.OPERATION_FAILED);
        }

        // 删除文章搜索索引
        articleSearchIndexlService.deleteArticleIndex(id);
    }

    @Override
    public void updateStatus(String id, Integer status) {
        if (id == null || status == null) {
            throw new ArticleException(RestResultEnum.INVALID_PARAM);
        }
        if (status < 0 || status > 2) {
            throw new ArticleException(RestResultEnum.INVALID_PARAM);
        }

        try {
            Post post = this.getOne(new LambdaQueryWrapper<Post>()
                    .eq(Post::getId, Long.parseLong(id))
                    .eq(Post::getDelFlag, 0));

            if (post == null) {
                throw new ArticleException(RestResultEnum.ARTICLE_NOT_FOUND);
            }
            post.setStatus(status);
            this.updateById(post);

            // 状态为2时删除文章索引
            if (status == 2) {
                articleSearchIndexlService.deleteArticleIndex(id);
            }
            // 状态为0时重新提交审核
            else if (status == 0) {
                ArticleDTO articleDTO = new ArticleDTO();
                articleDTO.setArticleId(id);
                edgeClient.submitArticleAudit(articleDTO);
            }
        } catch (NumberFormatException e) {
            throw new ArticleException(RestResultEnum.INVALID_PARAM);
        } catch (Exception e) {
            throw new ArticleException(RestResultEnum.OPERATION_FAILED);
        }
    }
    
    @Override
    public PostCountVO getPostCount() {
        PostCountVO countVO = new PostCountVO();
        
        try {
            String popularCountStr = stringRedisTemplate.opsForValue().get(RedisConstants.ARTICLE_POPULAR_COUNT_KEY);
            String selectedCountStr = stringRedisTemplate.opsForValue().get(RedisConstants.ARTICLE_SELECTED_COUNT_KEY);
            
            //查询数据库并更新缓存
            if (popularCountStr == null || selectedCountStr == null) {
                // 防止缓存击穿
                String lockKey = RedisConstants.ARTICLE_COUNT_LOCK_PREFIX + "init";
                Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
                
                if (Boolean.TRUE.equals(lock)) {
                    try {
                        // 再次尝试从缓存获取
                        popularCountStr = stringRedisTemplate.opsForValue().get(RedisConstants.ARTICLE_POPULAR_COUNT_KEY);
                        selectedCountStr = stringRedisTemplate.opsForValue().get(RedisConstants.ARTICLE_SELECTED_COUNT_KEY);
                        if (popularCountStr == null || selectedCountStr == null) {
                            Long popularCount = this.count(new LambdaQueryWrapper<Post>()
                                    .eq(Post::getDelFlag, 0)
                                    .eq(Post::getStatus, 1)
                                    .eq(Post::getTopTag, "popular"));

                            Long selectedCount = this.count(new LambdaQueryWrapper<Post>()
                                    .eq(Post::getDelFlag, 0)
                                    .eq(Post::getStatus, 1)
                                    .eq(Post::getTopTag, "selected"));

                            stringRedisTemplate.opsForValue().set(
                                    RedisConstants.ARTICLE_POPULAR_COUNT_KEY, 
                                    String.valueOf(popularCount), 
                                    RedisConstants.CACHE_EXPIRE_TIME, 
                                    TimeUnit.HOURS);
                            
                            stringRedisTemplate.opsForValue().set(
                                    RedisConstants.ARTICLE_SELECTED_COUNT_KEY, 
                                    String.valueOf(selectedCount), 
                                    RedisConstants.CACHE_EXPIRE_TIME, 
                                    TimeUnit.HOURS);

                            countVO.setPopularCount(popularCount);
                            countVO.setSelectedCount(selectedCount);
                        } else {
                            // 使用缓存中的数据
                            countVO.setPopularCount(Long.valueOf(popularCountStr));
                            countVO.setSelectedCount(Long.valueOf(selectedCountStr));
                        }
                    } finally {
                        stringRedisTemplate.delete(lockKey);
                    }
                } else {
                    // 获取锁失败等待
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    // 重新尝试从缓存获取数据
                    popularCountStr = stringRedisTemplate.opsForValue().get(RedisConstants.ARTICLE_POPULAR_COUNT_KEY);
                    selectedCountStr = stringRedisTemplate.opsForValue().get(RedisConstants.ARTICLE_SELECTED_COUNT_KEY);
                    
                    // 如果缓存中有数据，则使用缓存数据
                    if (popularCountStr != null && selectedCountStr != null) {
                        countVO.setPopularCount(Long.valueOf(popularCountStr));
                        countVO.setSelectedCount(Long.valueOf(selectedCountStr));
                    } else {
                        // 如果缓存中仍然没有数据，返回默认值0
                        countVO.setPopularCount(0L);
                        countVO.setSelectedCount(0L);
                    }
                }
            } else {
                // 使用缓存中的数据
                countVO.setPopularCount(Long.valueOf(popularCountStr));
                countVO.setSelectedCount(Long.valueOf(selectedCountStr));
            }
        } catch (Exception e) {
            log.error("获取文章数量统计失败", e);
            // 发生异常时返回默认值0
            countVO.setPopularCount(0L);
            countVO.setSelectedCount(0L);
        }
        
        return countVO;
    }
    
    /**
     * 定时刷新文章数量缓存，每10分钟执行一次
     */
    @Scheduled(fixedRateString = "${article.count.refresh.rate:600000}")
    public void refreshArticleCountCache() {
        try {
            log.info("开始刷新文章数量缓存");
            
            // 查询热门文章数量
            Long popularCount = this.count(new LambdaQueryWrapper<Post>()
                    .eq(Post::getDelFlag, 0)
                    .eq(Post::getStatus, 1)
                    .eq(Post::getTopTag, "popular"));
            
            // 查询精选文章数量
            Long selectedCount = this.count(new LambdaQueryWrapper<Post>()
                    .eq(Post::getDelFlag, 0)
                    .eq(Post::getStatus, 1)
                    .eq(Post::getTopTag, "selected"));
            
            // 更新缓存
            stringRedisTemplate.opsForValue().set(
                    RedisConstants.ARTICLE_POPULAR_COUNT_KEY, 
                    String.valueOf(popularCount), 
                    RedisConstants.CACHE_EXPIRE_TIME, 
                    TimeUnit.HOURS);
            
            stringRedisTemplate.opsForValue().set(
                    RedisConstants.ARTICLE_SELECTED_COUNT_KEY, 
                    String.valueOf(selectedCount), 
                    RedisConstants.CACHE_EXPIRE_TIME, 
                    TimeUnit.HOURS);
            
            log.info("文章数量缓存刷新完成，热门文章：{}，精选文章：{}", popularCount, selectedCount);
        } catch (Exception e) {
            log.error("刷新文章数量缓存失败", e);
        }
    }
}
