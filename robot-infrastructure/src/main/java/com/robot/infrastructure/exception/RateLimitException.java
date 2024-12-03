package com.robot.infrastructure.exception;

import com.robot.infrastructure.model.RateLimitResponse;
import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException {
    
    private final RateLimitResponse response;
    
    public RateLimitException(RateLimitResponse response) {
        super(response.getMessage());
        this.response = response;
    }
} 