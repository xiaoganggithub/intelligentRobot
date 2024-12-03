package com.robot.infrastructure.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    
    @Bean
    @RefreshScope
    public RestTemplate restTemplate(RestTemplateBuilder builder, RobotProperties properties) {
        return builder
            .setConnectTimeout(Duration.ofMillis(properties.getApi().getConnectTimeout()))
            .setReadTimeout(Duration.ofMillis(properties.getApi().getReadTimeout()))
            .build();
    }
} 