package com.vortex.worker.core.entities;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class DatabaseInstance {

    private final UUID id;
    private final String name;
    private final String host;
    private final String status;
    private final LocalDateTime registeredAt;

    public DatabaseInstance(UUID id, String name, String host, String status) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.status = status;
        this.registeredAt = LocalDateTime.now();
    }
}