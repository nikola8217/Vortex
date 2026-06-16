package com.vortex.worker.core.entities;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ProcessedMetric {

    private final UUID id;
    private final UUID instanceId;
    private final double avgQueryCount;
    private final double avgSlowQueries;
    private final double avgConnections;
    private final double avgCacheHitRate;
    private final double avgDiskIo;
    private final String period;
    private final LocalDateTime processedAt;

    public ProcessedMetric(UUID id, UUID instanceId, double avgQueryCount, double avgSlowQueries,
                           double avgConnections, double avgCacheHitRate, double avgDiskIo, String period) {
        this.id = id;
        this.instanceId = instanceId;
        this.avgQueryCount = avgQueryCount;
        this.avgSlowQueries = avgSlowQueries;
        this.avgConnections = avgConnections;
        this.avgCacheHitRate = avgCacheHitRate;
        this.avgDiskIo = avgDiskIo;
        this.period = period;
        this.processedAt = LocalDateTime.now();
    }
}