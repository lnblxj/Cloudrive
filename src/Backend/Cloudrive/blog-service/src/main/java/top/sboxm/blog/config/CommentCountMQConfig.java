package top.sboxm.blog.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommentCountMQConfig {
    // 评论计数队列
    public static final String COMMENT_COUNT_QUEUE = "article.comment.count.queue";
    public static final String COMMENT_COUNT_EXCHANGE = "article.comment.count.exchange";
    public static final String COMMENT_COUNT_ROUTING_KEY = "article.comment.count";

    @Bean
    public Queue commentCountQueue() {
        return QueueBuilder.durable(COMMENT_COUNT_QUEUE)
                .build();
    }

    @Bean
    public DirectExchange commentCountExchange() {
        return new DirectExchange(COMMENT_COUNT_EXCHANGE);
    }

    @Bean
    public Binding commentCountBinding() {
        return BindingBuilder.bind(commentCountQueue())
                .to(commentCountExchange())
                .with(COMMENT_COUNT_ROUTING_KEY);
    }
}