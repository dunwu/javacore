package io.github.dunwu.javacore.web;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Spring Boot 集成 MyBatis-Plus 配置
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see <a href="https://mybatis.plus/">MyBatis-Plus</a>
 * @since 2019-04-27
 */
@Configuration
public class CustomConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
