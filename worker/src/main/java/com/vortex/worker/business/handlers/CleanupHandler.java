package com.vortex.worker.business.handlers;

import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskType;
import com.vortex.worker.business.ports.IRawMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CleanupHandler implements TaskHandler {

    private final IRawMetricRepository rawMetricRepository;

    @Override
    public boolean supports(TaskType type) {
        return type == TaskType.CLEANUP;
    }

    @Override
    public void handle(Task task) {
        int olderThanDays = 30;
        if (task.getPayload() != null && task.getPayload().containsKey("olderThanDays")) {
            olderThanDays = (int) task.getPayload().get("olderThanDays");
        }

        LocalDateTime cutoff = LocalDateTime.now().minusDays(olderThanDays);
        rawMetricRepository.deleteOlderThan(cutoff);
        log.info("Cleanup completed — deleted metrics older than {} days", olderThanDays);
    }
}