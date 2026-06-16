package com.vortex.worker.business.ports;

import com.vortex.worker.core.entities.DatabaseInstance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDatabaseInstanceRepository {
    List<DatabaseInstance> findAll();
    DatabaseInstance save(DatabaseInstance instance);
    Optional<DatabaseInstance> findById(UUID id);
}