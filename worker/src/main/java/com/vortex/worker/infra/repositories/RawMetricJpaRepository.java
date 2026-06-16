package com.vortex.worker.infra.repositories;

import com.vortex.worker.infra.models.RawMetricModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RawMetricJpaRepository extends JpaRepository<RawMetricModel, UUID> {

    List<RawMetricModel> findByInstanceIdAndStatusAndTimestampBetween(
            UUID instanceId, String status, LocalDateTime from, LocalDateTime to);

    @Modifying
    @Transactional
    @Query("DELETE FROM RawMetricModel r WHERE r.timestamp < :date")
    void deleteByTimestampBefore(LocalDateTime date);
}