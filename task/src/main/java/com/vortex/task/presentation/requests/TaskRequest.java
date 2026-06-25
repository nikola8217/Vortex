package com.vortex.task.presentation.requests;

import com.vortex.task.business.dtos.TaskDto;
import com.vortex.task.presentation.httpValidations.TaskRequestValidation;
import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class TaskRequest {
    private String name;
    private TaskType type;
    private TaskPriority priority;
    private Map<String, Object> payload;
    private int maxRetries;
    private int timeoutSeconds;
    private String cronExpression;

    public TaskDto format() {
        TaskRequestValidation.validate(this);
        return new TaskDto(
                name,
                type,
                priority,
                payload,
                maxRetries,
                timeoutSeconds,
                cronExpression
        );
    }
}