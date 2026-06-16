package com.vortex.scheduler.infra.mappers;

import com.vortex.scheduler.core.entities.ScheduledJob;
import com.vortex.scheduler.infra.models.ScheduledJobModel;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJobMapper {

    public ScheduledJob toDomain(ScheduledJobModel model) {
        return new ScheduledJob(
                model.getId(),
                model.getName(),
                model.getType(),
                model.getPriority(),
                model.getCronExpression(),
                model.getMaxRetries(),
                model.getTimeoutSeconds(),
                model.getPayload(),
                model.isActive()
        );
    }

    public ScheduledJobModel toModel(ScheduledJob job) {
        ScheduledJobModel model = new ScheduledJobModel();
        model.setId(job.getId());
        model.setName(job.getName());
        model.setType(job.getType());
        model.setPriority(job.getPriority());
        model.setCronExpression(job.getCronExpression());
        model.setMaxRetries(job.getMaxRetries());
        model.setTimeoutSeconds(job.getTimeoutSeconds());
        model.setPayload(job.getPayload());
        model.setActive(job.isActive());
        model.setCreatedAt(job.getCreatedAt());
        model.setLastTriggeredAt(job.getLastTriggeredAt());
        return model;
    }
}