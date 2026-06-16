package com.vortex.worker.business.handlers;

import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskType;
import com.vortex.worker.business.ports.IDatabaseInstanceRepository;
import com.vortex.worker.business.ports.IRawMetricRepository;
import com.vortex.worker.core.entities.DatabaseInstance;
import com.vortex.worker.core.entities.RawMetric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class QualityCheckHandler implements TaskHandler {

    private final IDatabaseInstanceRepository instanceRepository;
    private final IRawMetricRepository rawMetricRepository;

    @Override
    public boolean supports(TaskType type) {
        return type == TaskType.QUALITY_CHECK;
    }

    @Override
    public void handle(Task task) {
        int slowQueriesThreshold = 100;
        int connectionsThreshold = 80;

        if (task.getPayload() != null) {
            if (task.getPayload().containsKey("slowQueriesThreshold")) {
                slowQueriesThreshold = (int) task.getPayload().get("slowQueriesThreshold");
            }
            if (task.getPayload().containsKey("connectionsThreshold")) {
                connectionsThreshold = (int) task.getPayload().get("connectionsThreshold");
            }
        }

        List<DatabaseInstance> instances = instanceRepository.findAll();
        LocalDateTime from = LocalDateTime.now().minusMinutes(15);
        LocalDateTime to = LocalDateTime.now();

        for (DatabaseInstance instance : instances) {
            List<RawMetric> metrics = rawMetricRepository
                    .findValidByInstanceIdAndPeriod(instance.getId(), from, to);

            for (RawMetric metric : metrics) {
                if (metric.getSlowQueries() > slowQueriesThreshold) {
                    log.warn("ALERT: Instance {} has {} slow queries (threshold: {})",
                            instance.getName(), metric.getSlowQueries(), slowQueriesThreshold);
                }
                if (metric.getActiveConnections() > connectionsThreshold) {
                    log.warn("ALERT: Instance {} has {} active connections (threshold: {})",
                            instance.getName(), metric.getActiveConnections(), connectionsThreshold);
                }
            }
        }

        log.info("Quality check completed for {} instances", instances.size());
    }
}