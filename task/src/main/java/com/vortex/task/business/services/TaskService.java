package com.vortex.task.business.services;

import com.vortex.task.business.dtos.TaskDto;
import com.vortex.task.business.ports.ITaskRepository;
import com.vortex.task.exceptions.TaskException;
import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskStatus;
import com.vortex.shared.events.TaskSubmittedEvent;
import com.vortex.shared.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final ITaskRepository taskRepository;
    private final KafkaProducer kafkaProducer;

    public Task submitTask(TaskDto dto) {
        Task task = Task.builder()
                .id(UUID.randomUUID())
                .name(dto.name())
                .type(dto.type())
                .priority(dto.priority())
                .status(TaskStatus.PENDING)
                .payload(dto.payload())
                .maxRetries(dto.maxRetries())
                .retryCount(0)
                .timeoutSeconds(dto.timeoutSeconds())
                .cronExpression(dto.cronExpression())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .scheduledAt(null)
                .build();

        Task saved = taskRepository.save(task);

        kafkaProducer.send("task-submitted", saved.getId().toString(),
                new TaskSubmittedEvent(UUID.randomUUID(), saved.getId(), saved, LocalDateTime.now()));

        log.info("Task submitted: {}", saved.getName());
        return saved;
    }

    public Task getTask(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskException("Task not found: " + id, HttpStatus.NOT_FOUND));
    }

    public List<Task> getTasks(TaskStatus status, TaskPriority priority) {
        if (status != null && priority != null) {
            return taskRepository.findByStatusAndPriority(status, priority);
        } else if (status != null) {
            return taskRepository.findByStatus(status);
        } else if (priority != null) {
            return taskRepository.findByPriority(priority);
        }
        return taskRepository.findByStatus(null);
    }

    public void cancelTask(UUID id) {
        Task task = getTask(id);
        if (task.getStatus() == TaskStatus.RUNNING) {
            throw new TaskException("Cannot cancel a running task", HttpStatus.CONFLICT);
        }
        Task cancelled = Task.builder()
                .id(task.getId())
                .name(task.getName())
                .type(task.getType())
                .priority(task.getPriority())
                .status(TaskStatus.CANCELLED)
                .payload(task.getPayload())
                .maxRetries(task.getMaxRetries())
                .retryCount(task.getRetryCount())
                .timeoutSeconds(task.getTimeoutSeconds())
                .cronExpression(task.getCronExpression())
                .createdAt(task.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .scheduledAt(task.getScheduledAt())
                .build();

        taskRepository.save(cancelled);
        log.info("Task cancelled: {}", id);
    }

    public Task retryTask(UUID id) {
        Task task = getTask(id);
        if (task.getStatus() != TaskStatus.FAILED && task.getStatus() != TaskStatus.DEAD) {
            throw new TaskException("Only failed or dead tasks can be retried", HttpStatus.CONFLICT);
        }

        Task retried = Task.builder()
                .id(task.getId())
                .name(task.getName())
                .type(task.getType())
                .priority(task.getPriority())
                .status(TaskStatus.PENDING)
                .payload(task.getPayload())
                .maxRetries(task.getMaxRetries())
                .retryCount(0)
                .timeoutSeconds(task.getTimeoutSeconds())
                .cronExpression(task.getCronExpression())
                .createdAt(task.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .scheduledAt(null)
                .build();

        Task saved = taskRepository.save(retried);
        kafkaProducer.send("task-submitted", saved.getId().toString(),
                new TaskSubmittedEvent(UUID.randomUUID(), saved.getId(), saved, LocalDateTime.now()));

        log.info("Task retried: {}", id);
        return saved;
    }
}