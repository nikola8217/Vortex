package com.vortex.worker.infra.repositories;

import com.vortex.worker.infra.models.ReportModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportJpaRepository extends JpaRepository<ReportModel, UUID> {

    List<ReportModel> findByReportType(String reportType);
}