package com.robot.common.exception;

public class ApiCallException extends RuntimeException {
    
    public ApiCallException(String message) {
        super(message);
    }
    
    public ApiCallException(String message, Throwable cause) {
        super(message, cause);
    }
} 