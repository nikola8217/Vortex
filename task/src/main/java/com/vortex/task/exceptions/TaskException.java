package com.vortex.task.exceptions;

import com.vortex.shared.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class TaskException extends AppException {
    public TaskException(String message, HttpStatus status) {
        super(message, status);
    }
}