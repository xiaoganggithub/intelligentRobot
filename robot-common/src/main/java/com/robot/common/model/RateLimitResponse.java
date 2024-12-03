package com.robot.common.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class RateLimitResponse {
    private String message;
    private int code;
    private String resourceName;
    private int limitQps;
    private long waitingTime;
} 