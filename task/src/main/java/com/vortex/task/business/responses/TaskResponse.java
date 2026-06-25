package com.vortex.task.business.responses;

import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskStatus;
import com.vortex.shared.enums.TaskType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String name,
        TaskType type,
        TaskPriority priority,
        TaskStatus status,
        Map<String, Object> payload,
        int maxRetries,
        int retryCount,
        int timeoutSeconds,
        String cronExpression,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime scheduledAt
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getName(),
                task.getType(),
                task.getPriority(),
                task.getStatus(),
                task.getPayload(),
                task.getMaxRetries(),
                task.getRetryCount(),
                task.getTimeoutSeconds(),
                task.getCronExpression(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getScheduledAt()
        );
    }
}