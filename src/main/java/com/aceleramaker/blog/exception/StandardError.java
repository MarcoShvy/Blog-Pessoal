package com.aceleramaker.blog.exception;

import java.time.LocalDateTime;

public class StandardError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;

    public StandardError(LocalDateTime timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
