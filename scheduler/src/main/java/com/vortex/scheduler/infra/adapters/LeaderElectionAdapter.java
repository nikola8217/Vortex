package com.vortex.scheduler.infra.adapters;

import com.vortex.scheduler.business.ports.ILeaderElection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeaderElectionAdapter implements ILeaderElection {

    @Value("${scheduler.leader-election.lock-id}")
    private long lockId;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean tryAcquireLock() {
        try {
            Boolean result = jdbcTemplate.queryForObject(
                    "SELECT pg_try_advisory_lock(?)",
                    Boolean.class,
                    lockId
            );
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Failed to acquire advisory lock", e);
            return false;
        }
    }

    @Override
    public void releaseLock() {
        try {
            jdbcTemplate.execute("SELECT pg_advisory_unlock(" + lockId + ")");
        } catch (Exception e) {
            log.error("Failed to release advisory lock", e);
        }
    }

    @Override
    public boolean isLeader() {
        try {
            Boolean result = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) > 0 FROM pg_locks WHERE locktype = 'advisory' AND objid = ?",
                    Boolean.class,
                    lockId
            );
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Failed to check leader status", e);
            return false;
        }
    }
}