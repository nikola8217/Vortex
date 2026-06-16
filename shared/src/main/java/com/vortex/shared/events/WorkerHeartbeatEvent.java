package com.vortex.shared.events;

import com.vortex.shared.entities.enums.WorkerStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record WorkerHeartbeatEvent(
        UUID eventId,
        UUID workerId,
        String host,
        int port,
        WorkerStatus status,
        LocalDateTime occurredAt
) {}