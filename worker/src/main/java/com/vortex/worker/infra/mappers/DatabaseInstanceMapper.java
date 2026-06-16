package com.vortex.worker.infra.mappers;

import com.vortex.worker.core.entities.DatabaseInstance;
import com.vortex.worker.infra.models.DatabaseInstanceModel;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInstanceMapper {

    public DatabaseInstance toDomain(DatabaseInstanceModel model) {
        return new DatabaseInstance(
                model.getId(),
                model.getName(),
                model.getHost(),
                model.getStatus()
        );
    }

    public DatabaseInstanceModel toModel(DatabaseInstance entity) {
        DatabaseInstanceModel model = new DatabaseInstanceModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setHost(entity.getHost());
        model.setStatus(entity.getStatus());
        model.setRegisteredAt(entity.getRegisteredAt());
        return model;
    }
}