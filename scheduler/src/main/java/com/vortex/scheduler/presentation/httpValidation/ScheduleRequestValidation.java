package com.vortex.scheduler.presentation.httpValidation;

import com.vortex.scheduler.core.exceptions.SchedulerException;
import com.vortex.scheduler.presentation.requests.ScheduleRequest;
import org.springframework.http.HttpStatus;

public class ScheduleRequestValidation {

    public static void validate(ScheduleRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new SchedulerException("Name is required", HttpStatus.BAD_REQUEST);
        }
        if (request.getType() == null) {
            throw new SchedulerException("Type is required", HttpStatus.BAD_REQUEST);
        }
        if (request.getPriority() == null) {
            throw new SchedulerException("Priority is required", HttpStatus.BAD_REQUEST);
        }
        if (request.getCronExpression() == null || request.getCronExpression().isBlank()) {
            throw new SchedulerException("Cron expression is required", HttpStatus.BAD_REQUEST);
        }
        if (request.getMaxRetries() < 0) {
            throw new SchedulerException("Max retries must be >= 0", HttpStatus.BAD_REQUEST);
        }
        if (request.getTimeoutSeconds() <= 0) {
            throw new SchedulerException("Timeout must be > 0", HttpStatus.BAD_REQUEST);
        }
    }
}