package com.vortex.observability.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VortexHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Health health() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Health.up()
                    .withDetail("database", "PostgreSQL is up")
                    .withDetail("status", "Vortex is healthy")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("database", "PostgreSQL is down")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}