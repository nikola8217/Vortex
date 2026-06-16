package com.vortex.worker.infra.repositories;

import com.vortex.worker.infra.models.WorkerModel;
import com.vortex.shared.enums.WorkerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkerJpaRepository extends JpaRepository<WorkerModel, UUID> {
    List<WorkerModel> findByStatus(WorkerStatus status);
}