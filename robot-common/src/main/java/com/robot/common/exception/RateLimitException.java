package com.robot.common.exception;

import com.robot.common.model.RateLimitResponse;
import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException {
    
    private final RateLimitResponse response;
    
    public RateLimitException(RateLimitResponse response) {
        super(response.getMessage());
        this.response = response;
    }
} 