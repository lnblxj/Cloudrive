package top.sboxm.edge.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // 文章审核队列
    public static final String ARTICLE_AUDIT_QUEUE = "article.audit.queue";
    public static final String ARTICLE_AUDIT_EXCHANGE = "article.audit.exchange";
    public static final String ARTICLE_AUDIT_ROUTING_KEY = "article.audit";

    // 邮件发送队列
    public static final String MAIL_SEND_QUEUE = "mail.send.queue";
    public static final String MAIL_SEND_EXCHANGE = "mail.send.exchange";
    public static final String MAIL_SEND_ROUTING_KEY = "mail.send";


    // ES同步队列
    public static final String ES_SYNC_QUEUE = "es.sync.queue";
    public static final String ES_SYNC_EXCHANGE = "es.sync.exchange";
    public static final String ES_SYNC_ROUTING_KEY = "es.sync";
    
    // ES同步死信队列
    public static final String ES_SYNC_DLX = "es.sync.dlx";
    public static final String ES_SYNC_DLQ = "es.sync.dlq";
    public static final String ES_SYNC_DLK = "es.sync.dlk";

    @Bean
    public Queue articleAuditQueue() {
        return new Queue(ARTICLE_AUDIT_QUEUE, true);
    }

    @Bean
    public DirectExchange articleAuditExchange() {
        return new DirectExchange(ARTICLE_AUDIT_EXCHANGE);
    }

    @Bean
    public Binding articleAuditBinding() {
        return BindingBuilder.bind(articleAuditQueue())
                .to(articleAuditExchange())
                .with(ARTICLE_AUDIT_ROUTING_KEY);
    }

    @Bean
    public Queue mailSendQueue() {
        return new Queue(MAIL_SEND_QUEUE, true);
    }

    @Bean
    public DirectExchange mailSendExchange() {
        return new DirectExchange(MAIL_SEND_EXCHANGE);
    }

    @Bean
    public Binding mailSendBinding() {
        return BindingBuilder.bind(mailSendQueue())
                .to(mailSendExchange())
                .with(MAIL_SEND_ROUTING_KEY);
    }

    @Bean
    public Queue esSyncQueue() {
        return QueueBuilder.durable(ES_SYNC_QUEUE)
                .withArgument("x-dead-letter-exchange", ES_SYNC_DLX)
                .withArgument("x-dead-letter-routing-key", ES_SYNC_DLK)
                // .withArgument("x-message-ttl", 60000) // 消息过期时间1分钟
                .withArgument("x-max-retries", 3) // 最大重试次数
                .build();
    }

    @Bean
    public Queue esSyncDLQ() {
        return new Queue(ES_SYNC_DLQ, true);
    }

    @Bean
    public DirectExchange esSyncExchange() {
        return new DirectExchange(ES_SYNC_EXCHANGE);
    }

    @Bean
    public DirectExchange esSyncDLX() {
        return new DirectExchange(ES_SYNC_DLX);
    }

    @Bean
    public Binding esSyncBinding() {
        return BindingBuilder.bind(esSyncQueue())
                .to(esSyncExchange())
                .with(ES_SYNC_ROUTING_KEY);
    }

    @Bean
    public Binding esSyncDLQBinding() {
        return BindingBuilder.bind(esSyncDLQ())
                .to(esSyncDLX())
                .with(ES_SYNC_DLK);
    }
}