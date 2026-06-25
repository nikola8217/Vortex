package com.vortex.scheduler.presentation.requests;

import com.vortex.scheduler.business.dtos.ScheduleDto;
import com.vortex.scheduler.presentation.httpValidation.ScheduleRequestValidation;
import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleRequest {
    private String name;
    private TaskType type;
    private TaskPriority priority;
    private String cronExpression;
    private int maxRetries;
    private int timeoutSeconds;
    private String payload;

    public ScheduleDto format() {
        ScheduleRequestValidation.validate(this);

        return new ScheduleDto(
                name,
                type,
                priority,
                cronExpression,
                maxRetries,
                timeoutSeconds,
                payload
        );
    }
}