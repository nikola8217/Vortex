package com.vortex.scheduler.core.entities;

import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ScheduledJob {

    private final UUID id;
    private final String name;
    private final TaskType type;
    private final TaskPriority priority;
    private final String cronExpression;
    private final int maxRetries;
    private final int timeoutSeconds;
    private final String payload;
    private final boolean active;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastTriggeredAt;

    public ScheduledJob(UUID id, String name, TaskType type, TaskPriority priority,
                        String cronExpression, int maxRetries, int timeoutSeconds,
                        String payload, boolean active) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.priority = priority;
        this.cronExpression = cronExpression;
        this.maxRetries = maxRetries;
        this.timeoutSeconds = timeoutSeconds;
        this.payload = payload;
        this.active = active;
        this.createdAt = LocalDateTime.now();
        this.lastTriggeredAt = null;
    }
}