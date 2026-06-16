package com.vortex.worker.core.entities;

import com.vortex.shared.enums.WorkerStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class WorkerEntity {

    private final UUID id;
    private final String host;
    private final int port;
    private final WorkerStatus status;
    private final LocalDateTime lastHeartbeat;
    private final LocalDateTime registeredAt;

    public WorkerEntity(UUID id, String host, int port, WorkerStatus status) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.status = status;
        this.lastHeartbeat = LocalDateTime.now();
        this.registeredAt = LocalDateTime.now();
    }
}