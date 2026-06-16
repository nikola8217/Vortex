package com.vortex.shared.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskScheduledEvent(
        UUID eventId,
        UUID taskId,
        UUID workerId,
        LocalDateTime occurredAt
) {}