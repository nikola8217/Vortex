package com.vortex.worker.infra.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "raw_metrics")
@Data
@NoArgsConstructor
public class RawMetricModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID instanceId;

    @Column(nullable = false)
    private int queryCount;

    @Column(nullable = false)
    private int slowQueries;

    @Column(nullable = false)
    private int activeConnections;

    @Column(nullable = false)
    private double cacheHitRate;

    @Column(nullable = false)
    private double diskIo;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String status;
}