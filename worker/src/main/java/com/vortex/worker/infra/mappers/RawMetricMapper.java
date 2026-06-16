package com.vortex.worker.infra.mappers;

import com.vortex.worker.core.entities.RawMetric;
import com.vortex.worker.infra.models.RawMetricModel;
import org.springframework.stereotype.Component;

@Component
public class RawMetricMapper {

    public RawMetric toDomain(RawMetricModel model) {
        return new RawMetric(
                model.getId(),
                model.getInstanceId(),
                model.getQueryCount(),
                model.getSlowQueries(),
                model.getActiveConnections(),
                model.getCacheHitRate(),
                model.getDiskIo(),
                model.getStatus()
        );
    }

    public RawMetricModel toModel(RawMetric entity) {
        RawMetricModel model = new RawMetricModel();
        model.setId(entity.getId());
        model.setInstanceId(entity.getInstanceId());
        model.setQueryCount(entity.getQueryCount());
        model.setSlowQueries(entity.getSlowQueries());
        model.setActiveConnections(entity.getActiveConnections());
        model.setCacheHitRate(entity.getCacheHitRate());
        model.setDiskIo(entity.getDiskIo());
        model.setTimestamp(entity.getTimestamp());
        model.setStatus(entity.getStatus());
        return model;
    }
}