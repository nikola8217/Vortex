package com.vortex.worker.infra.adapters;

import com.vortex.worker.business.ports.IDatabaseInstanceRepository;
import com.vortex.worker.core.entities.DatabaseInstance;
import com.vortex.worker.infra.mappers.DatabaseInstanceMapper;
import com.vortex.worker.infra.repositories.DatabaseInstanceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DatabaseInstanceRepositoryAdapter implements IDatabaseInstanceRepository {

    private final DatabaseInstanceJpaRepository jpaRepository;
    private final DatabaseInstanceMapper mapper;

    @Override
    public List<DatabaseInstance> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public DatabaseInstance save(DatabaseInstance instance) {
        return mapper.toDomain(jpaRepository.save(mapper.toModel(instance)));
    }

    @Override
    public Optional<DatabaseInstance> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}