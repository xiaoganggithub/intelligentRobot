package com.robot.api.exception;

import com.robot.api.model.ErrorResponse;
import com.robot.common.exception.ApiCallException;
import com.robot.common.exception.RateLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitException(RateLimitException ex, WebRequest request) {
        log.warn("Rate limit exceeded: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
            .code(429)
            .message(ex.getMessage())
            .path(request.getDescription(false))
            .timestamp(System.currentTimeMillis())
            .build();
        return new ResponseEntity<>(response, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(ApiCallException.class)
    public ResponseEntity<ErrorResponse> handleApiCallException(ApiCallException ex, WebRequest request) {
        log.error("API call failed: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
            .code(500)
            .message(ex.getMessage())
            .path(request.getDescription(false))
            .timestamp(System.currentTimeMillis())
            .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponse response = ErrorResponse.builder()
            .code(500)
            .message("An unexpected error occurred")
            .path(request.getDescription(false))
            .timestamp(System.currentTimeMillis())
            .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 