package com.vortex.observability.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskMetrics {

    private final Counter tasksSubmitted;
    private final Counter tasksCompleted;
    private final Counter tasksFailed;
    private final Counter tasksDead;

    public TaskMetrics(MeterRegistry registry) {
        this.tasksSubmitted = Counter.builder("vortex.tasks.submitted")
                .description("Total number of submitted tasks")
                .register(registry);

        this.tasksCompleted = Counter.builder("vortex.tasks.completed")
                .description("Total number of completed tasks")
                .register(registry);

        this.tasksFailed = Counter.builder("vortex.tasks.failed")
                .description("Total number of failed tasks")
                .register(registry);

        this.tasksDead = Counter.builder("vortex.tasks.dead")
                .description("Total number of dead tasks")
                .register(registry);
    }

    public void incrementSubmitted() {
        tasksSubmitted.increment();
    }

    public void incrementCompleted() {
        tasksCompleted.increment();
    }

    public void incrementFailed() {
        tasksFailed.increment();
    }

    public void incrementDead() {
        tasksDead.increment();
    }
}