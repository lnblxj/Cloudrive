package top.sboxm.user.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignConfig {
    @Bean
    public Retryer myRetryer() {
        // 初始间隔时间为100ms，最大间隔时间为1s，重试2次
        return new Retryer.Default(100,1,3);
    }
    @Bean
    Logger.Level feignLoggerLevel() {
        // 请求日志级别
        return Logger.Level.FULL;}
}