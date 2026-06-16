package com.vortex.scheduler.infra.adapters;

import com.vortex.scheduler.business.ports.IScheduledJobRepository;
import com.vortex.scheduler.core.entities.ScheduledJob;
import com.vortex.scheduler.infra.mappers.ScheduledJobMapper;
import com.vortex.scheduler.infra.repositories.ScheduledJobJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ScheduledJobRepositoryAdapter implements IScheduledJobRepository {

    private final ScheduledJobJpaRepository jpaRepository;
    private final ScheduledJobMapper mapper;

    @Override
    public ScheduledJob save(ScheduledJob job) {
        return mapper.toDomain(jpaRepository.save(mapper.toModel(job)));
    }

    @Override
    public Optional<ScheduledJob> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<ScheduledJob> findAllActive() {
        return jpaRepository.findByActiveTrue()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }
}