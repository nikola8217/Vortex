package com.vortex.scheduler.business.services;

import com.vortex.scheduler.business.ports.ILeaderElection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderElectionService {

    private final ILeaderElection leaderElection;

    private volatile boolean leader = false;

    @Scheduled(fixedDelay = 5000)
    public void electLeader() {
        if (leaderElection.tryAcquireLock()) {
            if (!leader) {
                log.info("This instance is now the LEADER");
            }
            leader = true;
        } else {
            if (leader) {
                log.info("This instance is no longer the LEADER");
            }
            leader = false;
        }
    }

    public boolean isLeader() {
        return leader;
    }
}