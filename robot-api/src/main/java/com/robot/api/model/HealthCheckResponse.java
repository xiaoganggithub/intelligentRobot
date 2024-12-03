package com.robot.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HealthCheckResponse {
    private int status;
    private String message;
    private long timestamp;
    private SystemInfo system;
    private ServiceInfo service;

    @Data
    @Builder
    public static class SystemInfo {
        private String javaVersion;
        private String osName;
        private long totalMemory;
        private long freeMemory;
        private int availableProcessors;
    }

    @Data
    @Builder
    public static class ServiceInfo {
        private String serviceName;
        private String version;
        private String environment;
        private long uptime;
        private String buildTime;
    }
} 