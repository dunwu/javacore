package io.github.dunwu.javacore.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootHelloWorldApplication {

    private static final Logger log = LoggerFactory.getLogger(SpringBootHelloWorldApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootHelloWorldApplication.class);
    }

    @Bean
    public CommandLineRunner run() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) {
                log.info("start");
            }
        };
    }

}
