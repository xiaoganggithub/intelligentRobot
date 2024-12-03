package com.robot.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private int code;
    private String message;
    private String path;
    private long timestamp;
} 