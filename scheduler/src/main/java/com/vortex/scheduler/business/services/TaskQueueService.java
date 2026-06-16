package com.vortex.scheduler.business.services;

import com.vortex.scheduler.business.ports.IScheduledJobRepository;
import com.vortex.shared.entities.Task;
import com.vortex.shared.events.TaskSubmittedEvent;
import com.vortex.shared.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskQueueService {

    private final IScheduledJobRepository jobRepository;
    private final KafkaProducer kafkaProducer;
    private final LeaderElectionService leaderElectionService;

    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>(
            100,
            Comparator.comparing(Task::getPriority).reversed()
    );

    public void enqueue(Task task) {
        queue.offer(task);
        log.info("Task enqueued: {} with priority {}", task.getName(), task.getPriority());
    }

    public void processQueue() {
        if (!leaderElectionService.isLeader()) {
            return;
        }

        Task task = queue.poll();
        if (task == null) {
            return;
        }

        log.info("Dispatching task: {}", task.getName());

        TaskSubmittedEvent event = new TaskSubmittedEvent(
                UUID.randomUUID(),
                task.getId(),
                task,
                LocalDateTime.now()
        );

        kafkaProducer.send("task-submitted", task.getId().toString(), event);
    }

    public List<Task> getQueueSnapshot() {
        return queue.stream()
                .sorted(Comparator.comparing(Task::getPriority).reversed())
                .toList();
    }
}