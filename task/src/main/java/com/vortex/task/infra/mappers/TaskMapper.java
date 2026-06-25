package com.vortex.task.infra.mappers;

import com.vortex.task.infra.models.TaskModel;
import com.vortex.shared.entities.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toDomain(TaskModel model) {
        return Task.builder()
                .id(model.getId())
                .name(model.getName())
                .type(model.getType())
                .priority(model.getPriority())
                .status(model.getStatus())
                .maxRetries(model.getMaxRetries())
                .retryCount(model.getRetryCount())
                .timeoutSeconds(model.getTimeoutSeconds())
                .cronExpression(model.getCronExpression())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .scheduledAt(model.getScheduledAt())
                .build();
    }

    public TaskModel toModel(Task task) {
        TaskModel model = new TaskModel();
        model.setId(task.getId());
        model.setName(task.getName());
        model.setType(task.getType());
        model.setPriority(task.getPriority());
        model.setStatus(task.getStatus());
        model.setMaxRetries(task.getMaxRetries());
        model.setRetryCount(task.getRetryCount());
        model.setTimeoutSeconds(task.getTimeoutSeconds());
        model.setCronExpression(task.getCronExpression());
        model.setCreatedAt(task.getCreatedAt());
        model.setUpdatedAt(task.getUpdatedAt());
        model.setScheduledAt(task.getScheduledAt());
        return model;
    }
}