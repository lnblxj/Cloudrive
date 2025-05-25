package top.sboxm.edge.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import top.sboxm.edge.config.RabbitMQConfig;
import top.sboxm.edge.mapper.PostMapper;
import top.sboxm.edge.pojo.po.Post;
import top.sboxm.edge.service.ArticleAuditService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleAuditServiceImpl implements ArticleAuditService {

    private final RabbitTemplate rabbitTemplate;
    private final PostMapper postMapper;

    @Override
    public void auditArticle(Long articleId) {
        log.info("收到文章审核请求，文章ID：{}", articleId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.ARTICLE_AUDIT_EXCHANGE, RabbitMQConfig.ARTICLE_AUDIT_ROUTING_KEY, articleId);
    }

    @RabbitListener(queues = RabbitMQConfig.ARTICLE_AUDIT_QUEUE)
    public void handleArticleAudit(Long articleId) {
        log.info("处理文章审核请求，文章ID：{}", articleId);
        // 从数据库查询文章
        Post post = postMapper.selectById(articleId);
        if (post == null) {
            log.warn("文章不存在，跳过审核，文章ID：{}", articleId);
            return;
        }

        try {
            // 模拟审核耗时
            Thread.sleep(2000);
            // 设置审核状态为正常
            post.setStatus(1);
            // 更新文章状态
            postMapper.updateById(post);
            log.info("文章审核完成，文章ID：{}", articleId);
            // 发送消息到ES同步队列
            rabbitTemplate.convertAndSend(RabbitMQConfig.ES_SYNC_EXCHANGE, RabbitMQConfig.ES_SYNC_ROUTING_KEY, post);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("文章审核过程中断", e);
            // 设置审核状态为审核中
            post.setStatus(0);
            postMapper.updateById(post);
        }
    }
}