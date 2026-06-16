package com.vortex.shared.entities;

import com.vortex.shared.enums.WorkerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Worker {

    private UUID id;
    private String host;
    private int port;
    private WorkerStatus status;
    private LocalDateTime lastHeartbeat;
    private LocalDateTime registeredAt;
}