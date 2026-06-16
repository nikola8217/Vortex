package com.vortex.worker.business.handlers;

import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskType;
import com.vortex.worker.business.ports.IDatabaseInstanceRepository;
import com.vortex.worker.business.ports.IProcessedMetricRepository;
import com.vortex.worker.business.ports.IRawMetricRepository;
import com.vortex.worker.core.entities.DatabaseInstance;
import com.vortex.worker.core.entities.ProcessedMetric;
import com.vortex.worker.core.entities.RawMetric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataProcessingHandler implements TaskHandler {

    private final IDatabaseInstanceRepository instanceRepository;
    private final IRawMetricRepository rawMetricRepository;
    private final IProcessedMetricRepository processedMetricRepository;

    @Override
    public boolean supports(TaskType type) {
        return type == TaskType.DATA_PROCESSING;
    }

    @Override
    public void handle(Task task) {
        int periodHours = 1;
        if (task.getPayload() != null && task.getPayload().containsKey("periodHours")) {
            periodHours = (int) task.getPayload().get("periodHours");
        }

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusHours(periodHours);

        List<DatabaseInstance> instances = instanceRepository.findAll();

        for (DatabaseInstance instance : instances) {
            List<RawMetric> metrics = rawMetricRepository
                    .findValidByInstanceIdAndPeriod(instance.getId(), from, to);

            if (metrics.isEmpty()) {
                log.info("No valid metrics for instance {}", instance.getName());
                continue;
            }

            double avgQueryCount = metrics.stream().mapToInt(RawMetric::getQueryCount).average().orElse(0);
            double avgSlowQueries = metrics.stream().mapToInt(RawMetric::getSlowQueries).average().orElse(0);
            double avgConnections = metrics.stream().mapToInt(RawMetric::getActiveConnections).average().orElse(0);
            double avgCacheHitRate = metrics.stream().mapToDouble(RawMetric::getCacheHitRate).average().orElse(0);
            double avgDiskIo = metrics.stream().mapToDouble(RawMetric::getDiskIo).average().orElse(0);

            ProcessedMetric processed = new ProcessedMetric(
                    UUID.randomUUID(),
                    instance.getId(),
                    avgQueryCount,
                    avgSlowQueries,
                    avgConnections,
                    avgCacheHitRate,
                    avgDiskIo,
                    from + " - " + to
            );

            processedMetricRepository.save(processed);

            log.info("Processed metrics for instance {}", instance.getName());
        }
    }
}