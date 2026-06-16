package com.vortex.worker.infra.repositories;

import com.vortex.worker.infra.models.ProcessedMetricModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProcessedMetricJpaRepository extends JpaRepository<ProcessedMetricModel, UUID> {

    List<ProcessedMetricModel> findByInstanceId(UUID instanceId);
}