package com.vortex.task.infra.adapters;

import com.vortex.task.business.ports.ITaskRepository;
import com.vortex.task.infra.mappers.TaskMapper;
import com.vortex.task.infra.repositories.TaskJpaRepository;
import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements ITaskRepository {

    private final TaskJpaRepository jpaRepository;
    private final TaskMapper mapper;

    @Override
    public Task save(Task task) {
        return mapper.toDomain(jpaRepository.save(mapper.toModel(task)));
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return jpaRepository.findByStatus(status)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> findByPriority(TaskPriority priority) {
        return jpaRepository.findByPriority(priority)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority) {
        return jpaRepository.findByStatusAndPriority(status, priority)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }
}