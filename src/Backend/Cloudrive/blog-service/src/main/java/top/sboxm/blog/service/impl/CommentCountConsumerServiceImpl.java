package top.sboxm.blog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import top.sboxm.blog.config.CommentCountMQConfig;
import top.sboxm.blog.mapper.PostMapper;
import top.sboxm.blog.pojo.po.Post;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class CommentCountConsumerServiceImpl {


    private final PostMapper postMapper;
    private final StringRedisTemplate stringRedisTemplate;
    
    // 批量处理大小
    private static final int BATCH_SIZE = 50;
    // Redis缓存过期时间（24小时）
    private static final long CACHE_EXPIRE_TIME = 24;
    
    // 存储待更新的文章ID和评论数
    private List<Post> batchList = new ArrayList<>();

    @RabbitListener(queues = CommentCountMQConfig.COMMENT_COUNT_QUEUE)
    public void handleCommentCount(String articleId) {
        Post post = new Post();
        post.setId(Long.parseLong(articleId));

        String commentCountKey = "article:commentCount:" + articleId;
        String commentCount = stringRedisTemplate.opsForValue().get(commentCountKey);
        post.setCommentCount(Long.parseLong(commentCount));
        synchronized (batchList) {
            batchList.add(post);
            
            // 当积累的更新数量达到批处理阈值时，执行批量更新
            if (batchList.size() >= BATCH_SIZE) {
                updateBatch();
            }
        }
    }
    
    private void updateBatch() {
        synchronized (batchList) {
            if (batchList.isEmpty()) {
                return;
            }
            
            try {
                // 批量更新文章评论数
                for (Post post : batchList) {
                    postMapper.updateById(post);
                }
            } catch (Exception e) {
                log.error("批量更新评论数据失败", e);
            } finally {
                // 清空批处理列表
                batchList.clear();
            }
        }
    }
    
    @Scheduled(fixedDelay = 60000) // 每60秒检查一次
    public void scheduledUpdate() {
        synchronized (batchList) {
            if (!batchList.isEmpty()) {
                updateBatch();
            }
        }
    }
}