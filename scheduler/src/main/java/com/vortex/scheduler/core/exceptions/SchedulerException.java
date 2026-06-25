package com.vortex.scheduler.core.exceptions;

import com.vortex.shared.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class SchedulerException extends AppException {
    public SchedulerException(String message, HttpStatus status) {
        super(message, status);
    }
}