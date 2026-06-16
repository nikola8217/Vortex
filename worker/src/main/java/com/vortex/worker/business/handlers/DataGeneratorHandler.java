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

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataGeneratorHandler implements TaskHandler {

    private final IDatabaseInstanceRepository instanceRepository;
    private final IRawMetricRepository metricRepository;
    private final Random random = new Random();

    @Override
    public boolean supports(TaskType type) {
        return type == TaskType.DATA_GENERATOR;
    }

    @Override
    public void handle(Task task) {
        List<DatabaseInstance> instances = instanceRepository.findAll();

        log.info("Generating metrics for {} instances", instances.size());

        double invalidRate = 0.1;

        if (task.getPayload() != null && task.getPayload().containsKey("invalidRate")) {
            invalidRate = (double) task.getPayload().get("invalidRate");
        }

        for (DatabaseInstance instance : instances) {
            String status = random.nextDouble() < invalidRate ? "INVALID" : "VALID";

            RawMetric metric = new RawMetric(
                    UUID.randomUUID(),
                    instance.getId(),
                    random.nextInt(1000),
                    random.nextInt(200),
                    random.nextInt(100),
                    random.nextDouble(),
                    random.nextDouble() * 100,
                    status
            );

            metricRepository.save(metric);
        }

        log.info("Metrics generated successfully");
    }
}