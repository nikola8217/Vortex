package com.vortex.worker.infra.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
public class ReportModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String reportType;

    @Column(columnDefinition = "jsonb")
    private String content;

    @Column(nullable = false)
    private LocalDateTime generatedAt;
}