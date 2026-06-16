package com.vortex.worker.business.ports;

import com.vortex.worker.core.entities.WorkerEntity;
import com.vortex.shared.enums.WorkerStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IWorkerRepository {

    WorkerEntity save(WorkerEntity worker);
    Optional<WorkerEntity> findById(UUID id);
    List<WorkerEntity> findByStatus(WorkerStatus status);
    void delete(UUID id);
}