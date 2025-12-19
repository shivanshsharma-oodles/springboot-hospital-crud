package com.yt.jpa.hospitalManagement.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Data
public class ApiError {
    private LocalDateTime timestamp;
    private HttpStatus statusCode;
    private String error;
    private Object details; // OPTIONAL (for validation errors)

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(String error, HttpStatus statusCode) {
        this();
        this.error = error;
        this.statusCode = statusCode;
    }

    public ApiError(String error, HttpStatus statusCode, Object details) {
        this(error, statusCode);
        this.details = details;
    }
}
