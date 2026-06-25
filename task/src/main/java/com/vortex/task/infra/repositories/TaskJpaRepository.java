package com.vortex.task.infra.repositories;

import com.vortex.task.infra.models.TaskModel;
import com.vortex.shared.enums.TaskStatus;
import com.vortex.shared.enums.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByStatus(TaskStatus status);
    List<TaskModel> findByPriority(TaskPriority priority);
    List<TaskModel> findByStatusAndPriority(TaskStatus status, TaskPriority priority);
}