package com.vortex.task.business.ports;

import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITaskRepository {
    Task save(Task task);
    Optional<Task> findById(UUID id);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(TaskPriority priority);
    List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);
    void delete(UUID id);
}