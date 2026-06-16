package com.vortex.shared.entities;

import com.vortex.shared.entities.enums.TaskPriority;
import com.vortex.shared.entities.enums.TaskStatus;
import com.vortex.shared.entities.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private UUID id;
    private String name;
    private TaskType type;
    private TaskPriority priority;
    private TaskStatus status;
    private Map<String, Object> payload;
    private int maxRetries;
    private int retryCount;
    private int timeoutSeconds;
    private String cronExpression;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime scheduledAt;
}