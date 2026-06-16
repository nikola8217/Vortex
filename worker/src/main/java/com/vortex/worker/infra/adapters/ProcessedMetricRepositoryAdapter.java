package com.vortex.worker.infra.adapters;

import com.vortex.worker.business.ports.IProcessedMetricRepository;
import com.vortex.worker.core.entities.ProcessedMetric;
import com.vortex.worker.infra.mappers.ProcessedMetricMapper;
import com.vortex.worker.infra.repositories.ProcessedMetricJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProcessedMetricRepositoryAdapter implements IProcessedMetricRepository {

    private final ProcessedMetricJpaRepository jpaRepository;
    private final ProcessedMetricMapper mapper;

    @Override
    public ProcessedMetric save(ProcessedMetric metric) {
        return mapper.toDomain(jpaRepository.save(mapper.toModel(metric)));
    }

    @Override
    public List<ProcessedMetric> findByInstanceId(UUID instanceId) {
        return jpaRepository.findByInstanceId(instanceId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}