package com.vortex.worker.business.ports;

import com.vortex.worker.core.entities.Report;

import java.util.List;

public interface IReportRepository {
    Report save(Report report);
    List<Report> findByReportType(String reportType);
}