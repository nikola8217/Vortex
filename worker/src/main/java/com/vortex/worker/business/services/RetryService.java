package com.vortex.worker.business.services;

import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskStatus;
import com.vortex.shared.events.TaskStatusEvent;
import com.vortex.shared.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryService {

    private final KafkaProducer kafkaProducer;

    public void handleFailure(Task task, String reason) {
        if (task.getRetryCount() < task.getMaxRetries()) {
            int nextRetry = task.getRetryCount() + 1;
            long backoffMs = (long) Math.pow(2, nextRetry) * 1000;

            log.warn("Task {} failed, retry {}/{} in {}ms",
                    task.getName(), nextRetry, task.getMaxRetries(), backoffMs);

            TaskStatusEvent event = new TaskStatusEvent(
                    UUID.randomUUID(),
                    task.getId(),
                    TaskStatus.FAILED,
                    TaskStatus.PENDING,
                    "Retry " + nextRetry + " of " + task.getMaxRetries(),
                    java.time.LocalDateTime.now()
            );

            kafkaProducer.send("task-status", task.getId().toString(), event);

        } else {
            log.error("Task {} exhausted all retries, sending to DLQ", task.getName());

            TaskStatusEvent event = new TaskStatusEvent(
                    UUID.randomUUID(),
                    task.getId(),
                    TaskStatus.FAILED,
                    TaskStatus.DEAD,
                    "Exhausted all retries: " + reason,
                    java.time.LocalDateTime.now()
            );

            kafkaProducer.send("task-dead-letter", task.getId().toString(), event);
        }
    }
}