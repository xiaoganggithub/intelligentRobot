package com.robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
@ComponentScan(basePackages = "com.robot")
public class RobotApplication {
    
    static {
        System.setProperty("project.name", "robot-service");
        // Ensure Nacos properties are set
        System.setProperty("spring.cloud.nacos.discovery.service", "robot-service");
        System.setProperty("spring.cloud.nacos.discovery.instance-enabled", "true");
    }
    
    public static void main(String[] args) {
        SpringApplication.run(RobotApplication.class, args);
    }
} 