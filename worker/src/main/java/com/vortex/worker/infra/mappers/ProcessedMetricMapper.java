package com.vortex.worker.infra.mappers;

import com.vortex.worker.core.entities.ProcessedMetric;
import com.vortex.worker.infra.models.ProcessedMetricModel;
import org.springframework.stereotype.Component;

@Component
public class ProcessedMetricMapper {

    public ProcessedMetric toDomain(ProcessedMetricModel model) {
        return new ProcessedMetric(
                model.getId(),
                model.getInstanceId(),
                model.getAvgQueryCount(),
                model.getAvgSlowQueries(),
                model.getAvgConnections(),
                model.getAvgCacheHitRate(),
                model.getAvgDiskIo(),
                model.getPeriod()
        );
    }

    public ProcessedMetricModel toModel(ProcessedMetric entity) {
        ProcessedMetricModel model = new ProcessedMetricModel();
        model.setId(entity.getId());
        model.setInstanceId(entity.getInstanceId());
        model.setAvgQueryCount(entity.getAvgQueryCount());
        model.setAvgSlowQueries(entity.getAvgSlowQueries());
        model.setAvgConnections(entity.getAvgConnections());
        model.setAvgCacheHitRate(entity.getAvgCacheHitRate());
        model.setAvgDiskIo(entity.getAvgDiskIo());
        model.setPeriod(entity.getPeriod());
        model.setProcessedAt(entity.getProcessedAt());
        return model;
    }
}