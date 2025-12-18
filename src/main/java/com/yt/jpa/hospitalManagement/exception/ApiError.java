package com.yt.jpa.hospitalManagement.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private LocalDateTime timestamp; // time of error
    private HttpStatus statusCode;
    private String error;

    public ApiError() {this.timestamp = LocalDateTime.now();}

    public ApiError(String error, HttpStatus statusCode) {
        this(); // reuse default constructor as Single source of truth for timestamp initialization.
        this.error = error;
        this.statusCode = statusCode;
    }
}
