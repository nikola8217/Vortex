package com.vortex.worker.infra.adapters;

import com.vortex.worker.business.ports.IRawMetricRepository;
import com.vortex.worker.core.entities.RawMetric;
import com.vortex.worker.infra.mappers.RawMetricMapper;
import com.vortex.worker.infra.repositories.RawMetricJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RawMetricRepositoryAdapter implements IRawMetricRepository {

    private final RawMetricJpaRepository jpaRepository;
    private final RawMetricMapper mapper;

    @Override
    public RawMetric save(RawMetric metric) {
        return mapper.toDomain(jpaRepository.save(mapper.toModel(metric)));
    }

    @Override
    public List<RawMetric> findValidByInstanceIdAndPeriod(UUID instanceId, LocalDateTime from, LocalDateTime to) {
        return jpaRepository.findByInstanceIdAndStatusAndTimestampBetween(instanceId, "VALID", from, to)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteOlderThan(LocalDateTime date) {
        jpaRepository.deleteByTimestampBefore(date);
    }
}