package com.vortex.scheduler.business.ports;

import com.vortex.scheduler.core.entities.ScheduledJob;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IScheduledJobRepository {
    ScheduledJob save(ScheduledJob job);
    Optional<ScheduledJob> findById(UUID id);
    List<ScheduledJob> findAllActive();
    void delete(UUID id);
}