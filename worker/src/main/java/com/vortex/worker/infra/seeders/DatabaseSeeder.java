package com.vortex.worker.infra.seeders;

import com.vortex.worker.business.ports.IDatabaseInstanceRepository;
import com.vortex.worker.core.entities.DatabaseInstance;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder {

    private final IDatabaseInstanceRepository instanceRepository;

    @PostConstruct
    public void seed() {
        try {
            List<DatabaseInstance> existing = instanceRepository.findAll();
            if (!existing.isEmpty()) {
                log.info("Database instances already seeded, skipping...");
                return;
            }

            List<DatabaseInstance> instances = List.of(
                    new DatabaseInstance(UUID.randomUUID(), "db-primary", "192.168.1.1", "ONLINE"),
                    new DatabaseInstance(UUID.randomUUID(), "db-replica-1", "192.168.1.2", "ONLINE"),
                    new DatabaseInstance(UUID.randomUUID(), "db-replica-2", "192.168.1.3", "ONLINE"),
                    new DatabaseInstance(UUID.randomUUID(), "db-analytics", "192.168.1.4", "ONLINE"),
                    new DatabaseInstance(UUID.randomUUID(), "db-reporting", "192.168.1.5", "OFFLINE")
            );

            instances.forEach(instanceRepository::save);
            log.info("Seeded {} database instances", instances.size());
        } catch (Exception e) {
            log.warn("Seeding skipped, another instance already seeded: {}", e.getMessage());
        }
    }
}