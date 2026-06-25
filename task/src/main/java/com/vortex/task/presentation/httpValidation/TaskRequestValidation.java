package com.vortex.task.presentation.httpValidation;

import com.vortex.task.exceptions.ApiException;
import com.vortex.task.presentation.requests.TaskRequest;
import org.springframework.http.HttpStatus;

public class TaskRequestValidation {

    public static void validate(TaskRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ApiException("Name is required", HttpStatus.BAD_REQUEST);
        }
        if (request.getType() == null) {
            throw new ApiException("Type is required", HttpStatus.BAD_REQUEST);
        }
        if (request.getPriority() == null) {
            throw new ApiException("Priority is required", HttpStatus.BAD_REQUEST);
        }
        if (request.getMaxRetries() < 0) {
            throw new ApiException("Max retries must be >= 0", HttpStatus.BAD_REQUEST);
        }
        if (request.getTimeoutSeconds() <= 0) {
            throw new ApiException("Timeout must be > 0", HttpStatus.BAD_REQUEST);
        }
    }
}