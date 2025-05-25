package top.sboxm.blog.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ViewCountMQConfig {
    // 文章阅读计数队列
    public static final String VIEW_COUNT_QUEUE = "article.view.count.queue";
    public static final String VIEW_COUNT_EXCHANGE = "article.view.count.exchange";
    public static final String VIEW_COUNT_ROUTING_KEY = "article.view.count";

    @Bean
    public Queue viewCountQueue() {
        return QueueBuilder.durable(VIEW_COUNT_QUEUE)
                .build();
    }

    @Bean
    public DirectExchange viewCountExchange() {
        return new DirectExchange(VIEW_COUNT_EXCHANGE);
    }

    @Bean
    public Binding viewCountBinding() {
        return BindingBuilder.bind(viewCountQueue())
                .to(viewCountExchange())
                .with(VIEW_COUNT_ROUTING_KEY);
    }
}