package top.sboxm.blog.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sboxm.blog.config.CommentCountMQConfig;
import top.sboxm.blog.exceptions.CommentException;
import top.sboxm.blog.mapper.CommentMapper;
import top.sboxm.blog.pojo.dto.CommentAddDTO;
import top.sboxm.blog.pojo.dto.CommentUpdateDTO;
import top.sboxm.blog.pojo.po.Comment;
import top.sboxm.blog.pojo.vo.CommentVO;
import top.sboxm.blog.remote.UserClient;
import top.sboxm.blog.service.CommentService;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.result.RestResult;
import top.sboxm.common.utils.SecurityContextUtil;
import top.sboxm.blog.pojo.dto.UserInfoDTO;


import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addComment(CommentAddDTO dto) {
        // 参数校验
        if (dto == null || dto.getArticleId() == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new CommentException(RestResultEnum.INVALID_PARAM);
        }

        String userId = SecurityContextUtil.getUserId();
        if (userId == null) {
            throw new CommentException(RestResultEnum.NEED_LOGIN);
        }
        RestResult<UserInfoDTO> userInfoResult = userClient.getUserInfoById(userId);
        if (!userInfoResult.getIsSuccess() || userInfoResult.getCode() != 200 || userInfoResult.getData() == null) {
            throw new CommentException(RestResultEnum.SYSTEM_ERROR);
        }
        UserInfoDTO userInfo = userInfoResult.getData();
        
        Comment comment = new Comment();
        comment.setId(IdUtil.getSnowflake().nextId());
        comment.setArticleId(Long.parseLong(dto.getArticleId()));
        comment.setContent(dto.getContent());
        comment.setDelFlag(0);
        comment.setCreateBy(Long.parseLong(userId));
        comment.setUpdateBy(Long.parseLong(userId));
        comment.setNickName(userInfo.getNickname());
        comment.setAvatar(userInfo.getAvatar());
        save(comment);

        // 更新评论计数
        try {
            String commentCountKey = "article:commentCount:" + dto.getArticleId();
            String cachedCount = stringRedisTemplate.opsForValue().get(commentCountKey);
            if (cachedCount == null) {
                LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Comment::getArticleId, Long.parseLong(dto.getArticleId()))
                        .eq(Comment::getDelFlag, 0);
                long count = count(wrapper);
                stringRedisTemplate.opsForValue().set(commentCountKey, String.valueOf(count), 24, TimeUnit.HOURS);
            } else {
                // 增加评论计数
                stringRedisTemplate.opsForValue().increment(commentCountKey);
            }
        } catch (Exception e) {
            log.error("更新评论计数缓存失败", e);
            // 缓存操作失败不影响评论添加的主要业务
        }

        // 发送消息到消息队列
        try {
            rabbitTemplate.convertAndSend(CommentCountMQConfig.COMMENT_COUNT_EXCHANGE,
                    CommentCountMQConfig.COMMENT_COUNT_ROUTING_KEY, dto.getArticleId());
        } catch (Exception e) {
            log.error("发送评论计数消息失败", e);
            // 消息发送失败不影响评论添加的主要业务
        }

        return comment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateComment(CommentUpdateDTO dto) {
        // 参数校验
        if (dto == null || dto.getId() == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new CommentException(RestResultEnum.INVALID_PARAM);
        }

        Comment comment = getById(dto.getId());
        if (comment == null) {
            throw new CommentException(RestResultEnum.COMMENT_NOT_FOUND);
        }
        String userId = SecurityContextUtil.getUserId();
        if (userId == null) {
            throw new CommentException(RestResultEnum.NEED_LOGIN);
        }
        if (!comment.getCreateBy().toString().equals(userId)) {
            throw new CommentException(RestResultEnum.NO_PERMISSION_COMMENT);
        }
        comment.setContent(dto.getContent());
        if (!updateById(comment)) {
            throw new CommentException(RestResultEnum.OPERATION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long id) {
        // 参数校验
        if (id == null) {
            throw new CommentException(RestResultEnum.INVALID_PARAM);
        }

        Comment comment = getById(id);
        if (comment == null) {
            throw new CommentException(RestResultEnum.COMMENT_NOT_FOUND);
        }
        String userId = SecurityContextUtil.getUserId();
        if (userId == null) {
            throw new CommentException(RestResultEnum.NEED_LOGIN);
        }
        if (!comment.getCreateBy().toString().equals(userId)) {
            throw new CommentException(RestResultEnum.NO_PERMISSION_COMMENT);
        }
        
        if (!removeById(id)) {
            throw new CommentException(RestResultEnum.OPERATION_FAILED);
        }

        // 更新评论计数
        try {
            String commentCountKey = "article:commentCount:" + comment.getArticleId();
            String cachedCount = stringRedisTemplate.opsForValue().get(commentCountKey);
            if (cachedCount != null) {
                // 减少评论计数
                stringRedisTemplate.opsForValue().decrement(commentCountKey);
                // 发送消息到消息队列
                rabbitTemplate.convertAndSend(CommentCountMQConfig.COMMENT_COUNT_EXCHANGE,
                        CommentCountMQConfig.COMMENT_COUNT_ROUTING_KEY, String.valueOf(comment.getArticleId()));
            }
        } catch (Exception e) {
            log.error("更新评论计数缓存失败", e);
            // 缓存操作失败不影响评论删除的主要业务
        }
    }

    @Override
    public List<CommentVO> getCommentList(Long articleId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId)
                .eq(Comment::getDelFlag, 0)
                .orderByDesc(Comment::getCreateTime);
        return list(wrapper).stream().map(comment -> {
            CommentVO vo = new CommentVO();
            BeanUtils.copyProperties(comment, vo);
            vo.setId(String.valueOf(comment.getId()));
            return vo;
        }).collect(Collectors.toList());
    }
}
