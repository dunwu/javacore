package io.github.dunwu.javacore.web;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Web 层自定义配置
 * <p>
 * 基于 Spring Boot 自动配置的 {@code RestTemplateBuilder} 构造 {@code RestTemplate} Bean。
 * 用 Builder 而非直接 {@code new RestTemplate()}，可以统一继承 Boot 预置的超时、消息转换器等配置
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-04-27
 */
@Configuration
public class CustomConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
