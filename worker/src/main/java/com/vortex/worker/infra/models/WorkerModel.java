package com.vortex.worker.infra.models;

import com.vortex.shared.enums.WorkerStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workers")
@Data
@NoArgsConstructor
public class WorkerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private int port;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkerStatus status;

    @Column(nullable = false)
    private LocalDateTime lastHeartbeat;

    @Column(nullable = false)
    private LocalDateTime registeredAt;
}