package com.vortex.task.business.dtos;

import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskType;

import java.util.Map;

public record TaskDto(
        String name,
        TaskType type,
        TaskPriority priority,
        Map<String, Object> payload,
        int maxRetries,
        int timeoutSeconds,
        String cronExpression
) {}