package com.vortex.worker.infra.consumers;

import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskStatus;
import com.vortex.shared.enums.WorkerStatus;
import com.vortex.shared.events.TaskStatusEvent;
import com.vortex.shared.kafka.KafkaProducer;
import com.vortex.worker.business.handlers.TaskHandler;
import com.vortex.worker.business.services.RetryService;
import com.vortex.worker.business.services.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskConsumer {

    private final List<TaskHandler> handlers;
    private final RetryService retryService;
    private final WorkerService workerService;
    private final KafkaProducer kafkaProducer;

    @KafkaListener(topics = "task-submitted", groupId = "worker-service")
    public void consume(Task task) {
        log.info("Received task: {} of type {}", task.getName(), task.getType());

        TaskHandler handler = handlers.stream()
                .filter(h -> h.supports(task.getType()))
                .findFirst()
                .orElse(null);

        if (handler == null) {
            log.error("No handler found for task type: {}", task.getType());
            return;
        }

        kafkaProducer.send("task-status", task.getId().toString(), new TaskStatusEvent(
                UUID.randomUUID(),
                task.getId(),
                TaskStatus.PENDING,
                TaskStatus.RUNNING,
                "Task started",
                LocalDateTime.now()
        ));

        try {
            workerService.updateStatus(workerService.getCurrentWorker().getId(),
                    WorkerStatus.BUSY);

            handler.handle(task);

            kafkaProducer.send("task-status", task.getId().toString(), new TaskStatusEvent(
                    UUID.randomUUID(),
                    task.getId(),
                    TaskStatus.RUNNING,
                    TaskStatus.COMPLETED,
                    "Task completed successfully",
                    LocalDateTime.now()
            ));

            log.info("Task completed: {}", task.getName());

        } catch (Exception e) {
            log.error("Task failed: {}", task.getName(), e);
            retryService.handleFailure(task, e.getMessage());
        } finally {
            workerService.updateStatus(workerService.getCurrentWorker().getId(),
                    WorkerStatus.IDLE);
        }
    }
}