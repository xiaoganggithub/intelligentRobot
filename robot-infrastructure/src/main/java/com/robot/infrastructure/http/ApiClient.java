package com.robot.infrastructure.http;

import com.robot.infrastructure.config.RobotProperties;
import com.robot.infrastructure.exception.ApiCallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ApiClient {
    private final RestTemplate restTemplate;
    private final RobotProperties properties;

    public ApiClient(RestTemplate restTemplate, RobotProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Retryable(
        value = {ApiCallException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public String sendCommand(String command) {
        try {
            log.info("Sending command: {}", command);
            String response = restTemplate.postForObject(
                properties.getApi().getBaseUrl() + "/command",
                command,
                String.class
            );
            log.info("Received response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error sending command: {}", e.getMessage());
            throw new ApiCallException("Failed to send command", e);
        }
    }

    public String getStatus() {
        try {
            log.info("Getting robot status");
            String status = restTemplate.getForObject(
                properties.getApi().getBaseUrl() + "/status",
                String.class
            );
            log.info("Received status: {}", status);
            return status;
        } catch (Exception e) {
            log.error("Error getting status: {}", e.getMessage());
            throw new ApiCallException("Failed to get status", e);
        }
    }
} 