package com.vortex.worker.infra.adapters;

import com.vortex.worker.business.ports.IReportRepository;
import com.vortex.worker.core.entities.Report;
import com.vortex.worker.infra.mappers.ReportMapper;
import com.vortex.worker.infra.repositories.ReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportRepositoryAdapter implements IReportRepository {

    private final ReportJpaRepository jpaRepository;
    private final ReportMapper mapper;

    @Override
    public Report save(Report report) {
        return mapper.toDomain(jpaRepository.save(mapper.toModel(report)));
    }

    @Override
    public List<Report> findByReportType(String reportType) {
        return jpaRepository.findByReportType(reportType)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}