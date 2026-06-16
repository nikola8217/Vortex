package com.vortex.worker.business.ports;

import com.vortex.worker.core.entities.RawMetric;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IRawMetricRepository {
    RawMetric save(RawMetric metric);
    List<RawMetric> findValidByInstanceIdAndPeriod(UUID instanceId, LocalDateTime from, LocalDateTime to);
    void deleteOlderThan(LocalDateTime date);
}