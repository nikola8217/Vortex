package com.vortex.worker.infra.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "database_instances")
@Data
@NoArgsConstructor
public class DatabaseInstanceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime registeredAt;
}