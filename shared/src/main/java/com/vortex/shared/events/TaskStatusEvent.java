package com.vortex.shared.events;

import com.vortex.shared.entities.enums.TaskStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskStatusEvent(
        UUID eventId,
        UUID taskId,
        TaskStatus previousStatus,
        TaskStatus currentStatus,
        String reason,
        LocalDateTime occurredAt
) {}