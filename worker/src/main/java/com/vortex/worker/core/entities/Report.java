package com.vortex.worker.core.entities;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Report {

    private final UUID id;
    private final String reportType;
    private final String content;
    private final LocalDateTime generatedAt;

    public Report(UUID id, String reportType, String content) {
        this.id = id;
        this.reportType = reportType;
        this.content = content;
        this.generatedAt = LocalDateTime.now();
    }
}