package com.vortex.scheduler.infra.models;

import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scheduled_jobs")
@Data
@NoArgsConstructor
public class ScheduledJobModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @Column(nullable = false)
    private String cronExpression;

    @Column(nullable = false)
    private int maxRetries;

    @Column(nullable = false)
    private int timeoutSeconds;

    @Column(columnDefinition = "jsonb")
    private String payload;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastTriggeredAt;
}