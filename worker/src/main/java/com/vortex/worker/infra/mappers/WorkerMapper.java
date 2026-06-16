package com.vortex.worker.infra.mappers;

import com.vortex.worker.core.entities.WorkerEntity;
import com.vortex.worker.infra.models.WorkerModel;
import org.springframework.stereotype.Component;

@Component
public class WorkerMapper {

    public WorkerEntity toDomain(WorkerModel model) {
        return new WorkerEntity(
                model.getId(),
                model.getHost(),
                model.getPort(),
                model.getStatus()
        );
    }

    public WorkerModel toModel(WorkerEntity entity) {
        WorkerModel model = new WorkerModel();
        model.setId(entity.getId());
        model.setHost(entity.getHost());
        model.setPort(entity.getPort());
        model.setStatus(entity.getStatus());
        model.setLastHeartbeat(entity.getLastHeartbeat());
        model.setRegisteredAt(entity.getRegisteredAt());
        return model;
    }
}