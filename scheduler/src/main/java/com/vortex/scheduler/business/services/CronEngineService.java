package com.vortex.scheduler.business.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CronEngineService {

    private final TaskQueueService taskQueueService;
    private final LeaderElectionService leaderElectionService;

    @Scheduled(fixedDelay = 5000)
    public void dispatchQueue() {
        if (!leaderElectionService.isLeader()) {
            return;
        }
        taskQueueService.processQueue();
    }
}