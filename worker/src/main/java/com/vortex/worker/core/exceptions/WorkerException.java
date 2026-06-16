package com.vortex.worker.core.exceptions;

import com.vortex.shared.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class WorkerException extends AppException {
    public WorkerException(String message, HttpStatus status) {
        super(message, status);
    }
}