package com.vortex.scheduler.infra.repositories;

import com.vortex.scheduler.infra.models.ScheduledJobModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduledJobJpaRepository extends JpaRepository<ScheduledJobModel, UUID> {
    List<ScheduledJobModel> findByActiveTrue();
}