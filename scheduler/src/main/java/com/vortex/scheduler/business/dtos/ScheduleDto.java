package com.vortex.scheduler.business.dtos;

import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskType;

import java.util.Map;

public record ScheduleDto(
        String name,
        TaskType type,
        TaskPriority priority,
        String cronExpression,
        int maxRetries,
        int timeoutSeconds,
        String payload
) {}