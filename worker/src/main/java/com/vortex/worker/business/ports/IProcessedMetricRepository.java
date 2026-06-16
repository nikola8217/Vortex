package com.vortex.worker.business.ports;

import com.vortex.worker.core.entities.ProcessedMetric;

import java.util.List;
import java.util.UUID;

public interface IProcessedMetricRepository {
    ProcessedMetric save(ProcessedMetric metric);
    List<ProcessedMetric> findByInstanceId(UUID instanceId);
}