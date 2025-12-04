package com.restapp;

import java.time.LocalDateTime;

public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timetamp;

    public ErrorResponse(int status, String message, LocalDateTime timetamp) {
        this.status = status;
        this.message = message;
        this.timetamp = timetamp;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimetamp() {
        return timetamp;
    }

    public void setTimetamp(LocalDateTime timetamp) {
        this.timetamp = timetamp;
    }
}
