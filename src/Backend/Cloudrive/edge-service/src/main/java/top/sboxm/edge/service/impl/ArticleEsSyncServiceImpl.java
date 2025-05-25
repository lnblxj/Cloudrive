package top.sboxm.edge.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import top.sboxm.edge.config.RabbitMQConfig;
import top.sboxm.edge.pojo.document.ArticleDocument;
import top.sboxm.edge.pojo.po.Post;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleEsSyncServiceImpl {

    private final ElasticsearchOperations elasticsearchOperations;

    @RabbitListener(queues = RabbitMQConfig.ES_SYNC_QUEUE)
    public void syncToEs(Post post) {
        log.info("收到文章ES同步请求，文章ID：{}，标题：{}", post.getId(), post.getTitle());
        try {
            ArticleDocument document = new ArticleDocument();
            document.setId(post.getId());
            document.setTitle(post.getTitle());
            document.setContent(post.getContent());
            document.setSummary(post.getSummary());

            elasticsearchOperations.save(document);
            log.info("文章ES同步成功，文章ID：{}", post.getId());
        } catch (Exception e) {
            log.error("文章ES同步失败，文章ID：{}", post.getId(), e);
            // 判断是否为可恢复错误
            if (isRecoverableError(e)) {
                throw new AmqpRejectAndDontRequeueException("ES同步失败（可恢复错误）", e);
            } else {
                log.error("检测到不可恢复错误，消息将被拒绝，文章ID：{}", post.getId(), e);
                throw new AmqpRejectAndDontRequeueException("ES同步失败（不可恢复错误）", e);
            }
        }
    }

    private boolean isRecoverableError(Exception e) {
        return e instanceof java.net.SocketTimeoutException ||
               e instanceof java.net.ConnectException ||
               e instanceof org.springframework.dao.DataAccessResourceFailureException;
    }
}