package com.vortex.shared.events;

import com.vortex.shared.entities.Task;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskSubmittedEvent(
        UUID eventId,
        UUID taskId,
        Task task,
        LocalDateTime occurredAt
) {}