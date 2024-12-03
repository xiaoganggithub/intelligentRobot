package com.robot.api.service;

import com.robot.common.model.HealthCheckResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class HealthCheckService {
    private static final long START_TIME = System.currentTimeMillis();
    
    @Value("${spring.profiles.active:unknown}")
    private String environment;
    
    @Value("${spring.application.name:robot-service}")
    private String serviceName;
    
    @Value("${spring.application.version:1.0.0}")
    private String version;

    public HealthCheckResponse checkHealth() {
        Runtime runtime = Runtime.getRuntime();
        
        HealthCheckResponse.SystemInfo systemInfo = HealthCheckResponse.SystemInfo.builder()
            .javaVersion(System.getProperty("java.version"))
            .osName(System.getProperty("os.name"))
            .totalMemory(runtime.totalMemory())
            .freeMemory(runtime.freeMemory())
            .availableProcessors(runtime.availableProcessors())
            .build();
            
        String buildTime = LocalDateTime.ofInstant(
            Instant.now(), 
            ZoneId.systemDefault()
        ).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        HealthCheckResponse.ServiceInfo serviceInfo = HealthCheckResponse.ServiceInfo.builder()
            .serviceName(serviceName)
            .version(version)
            .environment(environment)
            .uptime(System.currentTimeMillis() - START_TIME)
            .buildTime(buildTime)
            .build();
            
        return HealthCheckResponse.builder()
            .status(200)
            .message("Service is running")
            .timestamp(System.currentTimeMillis())
            .system(systemInfo)
            .service(serviceInfo)
            .build();
    }
} 