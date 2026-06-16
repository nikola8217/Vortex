package com.vortex.worker.infra.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "processed_metrics")
@Data
@NoArgsConstructor
public class ProcessedMetricModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID instanceId;

    @Column(nullable = false)
    private double avgQueryCount;

    @Column(nullable = false)
    private double avgSlowQueries;

    @Column(nullable = false)
    private double avgConnections;

    @Column(nullable = false)
    private double avgCacheHitRate;

    @Column(nullable = false)
    private double avgDiskIo;

    @Column(nullable = false)
    private String period;

    @Column(nullable = false)
    private LocalDateTime processedAt;
}