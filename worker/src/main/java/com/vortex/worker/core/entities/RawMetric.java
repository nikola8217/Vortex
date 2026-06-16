package com.vortex.worker.core.entities;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RawMetric {

    private final UUID id;
    private final UUID instanceId;
    private final int queryCount;
    private final int slowQueries;
    private final int activeConnections;
    private final double cacheHitRate;
    private final double diskIo;
    private final LocalDateTime timestamp;
    private final String status;

    public RawMetric(UUID id, UUID instanceId, int queryCount, int slowQueries,
                     int activeConnections, double cacheHitRate, double diskIo, String status) {
        this.id = id;
        this.instanceId = instanceId;
        this.queryCount = queryCount;
        this.slowQueries = slowQueries;
        this.activeConnections = activeConnections;
        this.cacheHitRate = cacheHitRate;
        this.diskIo = diskIo;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }
}