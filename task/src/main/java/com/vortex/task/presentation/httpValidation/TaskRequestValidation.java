package com.vortex.task.presentation.httpValidation;

import com.vortex.task.exceptions.TaskException;
import com.vortex.task.presentation.requests.TaskRequest;
import org.springframework.http.HttpStatus;

public class TaskRequestValidation {

    public static void validate(TaskRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new TaskException("Name is required", HttpStatus.BAD_REQUEST);
        }
        if (request.getType() == null) {
            throw new TaskException("Type is required", HttpStatus.BAD_REQUEST);
        }
        if (request.getPriority() == null) {
            throw new TaskException("Priority is required", HttpStatus.BAD_REQUEST);
        }
        if (request.getMaxRetries() < 0) {
            throw new TaskException("Max retries must be >= 0", HttpStatus.BAD_REQUEST);
        }
        if (request.getTimeoutSeconds() <= 0) {
            throw new TaskException("Timeout must be > 0", HttpStatus.BAD_REQUEST);
        }
    }
}