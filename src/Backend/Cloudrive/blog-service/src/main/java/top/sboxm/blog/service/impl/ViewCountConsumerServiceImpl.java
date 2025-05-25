package top.sboxm.blog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.sboxm.blog.config.ViewCountMQConfig;
import top.sboxm.blog.mapper.PostMapper;
import top.sboxm.blog.pojo.po.Post;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class ViewCountConsumerServiceImpl {

    private final PostMapper postMapper;
    private final StringRedisTemplate stringRedisTemplate;

    // 批量处理大小
    private static final int BATCH_SIZE = 100;
    // Redis缓存过期时间
    private static final long CACHE_EXPIRE_TIME = 24;

    // 存储待更新的文章ID和阅读数
    private List<Post> batchList = new ArrayList<>();

    @RabbitListener(queues = ViewCountMQConfig.VIEW_COUNT_QUEUE)
    public void handleViewCount(String articleId) {
        Post post = new Post();
        post.setId(Long.parseLong(articleId));

        // 从Redis获取最新的阅读数
        String viewCountKey = "article:viewCount:" + articleId;
        String viewCount = stringRedisTemplate.opsForValue().get(viewCountKey);
        post.setViewCount(Long.parseLong(viewCount));

        synchronized (batchList) {
            batchList.add(post);

            // 当积累的更新数量达到批处理阈值时，执行批量更新
            if (batchList.size() >= BATCH_SIZE) {
                updateBatch();
            }
        }
    }

    private void updateBatch() {
        if (batchList.isEmpty()) {
            return;
        }

        try {
            // 批量更新MySQL中的阅读数
            for (Post post : batchList) {
                postMapper.updateById(post);
            }
        } catch (Exception e) {
            log.error("批量更新阅读数失败", e);
        } finally {
            batchList.clear();
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