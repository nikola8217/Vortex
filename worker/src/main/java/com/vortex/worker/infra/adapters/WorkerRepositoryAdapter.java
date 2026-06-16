package com.vortex.worker.infra.adapters;

import com.vortex.shared.enums.WorkerStatus;
import com.vortex.worker.business.ports.IWorkerRepository;
import com.vortex.worker.core.entities.WorkerEntity;
import com.vortex.worker.infra.mappers.WorkerMapper;
import com.vortex.worker.infra.repositories.WorkerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WorkerRepositoryAdapter implements IWorkerRepository {

    private final WorkerJpaRepository jpaRepository;
    private final WorkerMapper mapper;

    @Override
    public WorkerEntity save(WorkerEntity worker) {
        return mapper.toDomain(jpaRepository.save(mapper.toModel(worker)));
    }

    @Override
    public Optional<WorkerEntity> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<WorkerEntity> findByStatus(WorkerStatus status) {
        return jpaRepository.findByStatus(status)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }
}