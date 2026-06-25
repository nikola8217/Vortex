package com.vortex.task.exceptions;

import com.vortex.shared.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class ApiException extends AppException {
    public ApiException(String message, HttpStatus status) {
        super(message, status);
    }
}