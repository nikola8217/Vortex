package com.vortex.worker.infra.mappers;

import com.vortex.worker.core.entities.Report;
import com.vortex.worker.infra.models.ReportModel;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public Report toDomain(ReportModel model) {
        return new Report(
                model.getId(),
                model.getReportType(),
                model.getContent()
        );
    }

    public ReportModel toModel(Report entity) {
        ReportModel model = new ReportModel();
        model.setId(entity.getId());
        model.setReportType(entity.getReportType());
        model.setContent(entity.getContent());
        model.setGeneratedAt(entity.getGeneratedAt());
        return model;
    }
}