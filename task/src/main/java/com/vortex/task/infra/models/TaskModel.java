package com.vortex.task.infra.models;

import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskStatus;
import com.vortex.shared.enums.TaskType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class TaskModel {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(columnDefinition = "jsonb")
    private String payload;

    @Column(nullable = false)
    private int maxRetries;

    @Column(nullable = false)
    private int retryCount;

    @Column(nullable = false)
    private int timeoutSeconds;

    private String cronExpression;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime scheduledAt;
}