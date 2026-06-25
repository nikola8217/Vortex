package com.vortex.scheduler.business.responses;

import com.vortex.scheduler.core.entities.ScheduledJob;
import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleResponse(
        UUID id,
        String name,
        TaskType type,
        TaskPriority priority,
        String cronExpression,
        int maxRetries,
        int timeoutSeconds,
        String payload,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime lastTriggeredAt
) {
    public static ScheduleResponse from(ScheduledJob job) {
        return new ScheduleResponse(
                job.getId(),
                job.getName(),
                job.getType(),
                job.getPriority(),
                job.getCronExpression(),
                job.getMaxRetries(),
                job.getTimeoutSeconds(),
                job.getPayload(),
                job.isActive(),
                job.getCreatedAt(),
                job.getLastTriggeredAt()
        );
    }
}