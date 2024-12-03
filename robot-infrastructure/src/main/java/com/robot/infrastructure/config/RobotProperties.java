package com.robot.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "robot")
public class RobotProperties {
    private Api api = new Api();
    private RateLimit rateLimit = new RateLimit();

    @Data
    public static class Api {
        private String baseUrl;
        private int connectTimeout = 5000;
        private int readTimeout = 5000;
        private int maxRetries = 3;
        private long retryDelay = 1000;
    }

    @Data
    public static class RateLimit {
        private int qps = 100;
        private int maxQueueingTimeMs = 500;
        private int warmUpPeriodSec = 10;
        private int maxWaitingTime = 1000;
        private String resourceName = "robot-api";
    }
} 