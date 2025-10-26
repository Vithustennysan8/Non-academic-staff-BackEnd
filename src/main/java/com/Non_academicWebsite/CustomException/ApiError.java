package com.Non_academicWebsite.CustomException;

import java.time.Instant;

public record ApiError(int code, String message, Instant timestamp, Object details) {
    public ApiError(int code, String message) {
        this(code, message, Instant.now(), null);
    }
}