package com.example.AWSDemo.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception for business logic errors
 */
public class BusinessException extends RuntimeException {
    
    private final HttpStatus status;
    private final String error;

    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.error = "Business Logic Error";
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.error = "Business Logic Error";
    }

    public BusinessException(String message, String error, HttpStatus status) {
        super(message);
        this.status = status;
        this.error = error;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.BAD_REQUEST;
        this.error = "Business Logic Error";
    }

    public BusinessException(String message, String error, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}