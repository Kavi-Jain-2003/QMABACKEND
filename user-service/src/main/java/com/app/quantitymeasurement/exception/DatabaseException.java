package com.app.quantitymeasurement.exception;

import java.time.LocalDateTime;

public class DatabaseException {
    private LocalDateTime timeStamps;
    private int status;
    private String error, message;

    public DatabaseException(LocalDateTime timeStamps, int status, String error, String message){
        this.timeStamps = timeStamps;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public LocalDateTime getTimeStamp(){ return timeStamps; }

    public int getStatus(){ return status; }

    public String getError(){ return error; }

    public String getMessage(){ return message; }
}