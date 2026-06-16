package com.vortex.worker.business.services;

import com.vortex.shared.enums.WorkerStatus;
import com.vortex.shared.events.WorkerHeartbeatEvent;
import com.vortex.shared.kafka.KafkaProducer;
import com.vortex.worker.business.ports.IWorkerRepository;
import com.vortex.worker.core.entities.WorkerEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeartbeatService {

    private final IWorkerRepository workerRepository;
    private final KafkaProducer kafkaProducer;

    @Value("${worker.host}")
    private String host;

    @Value("${worker.port}")
    private int port;

    private UUID workerId;

    @jakarta.annotation.PostConstruct
    public void registerWorker() {

        WorkerEntity worker = new WorkerEntity(
                UUID.randomUUID(),
                host,
                port,
                WorkerStatus.IDLE
        );

        WorkerEntity saved = workerRepository.save(worker);

        this.workerId = saved.getId();

        log.info("Worker registered with id: {}", workerId);
    }

    @Scheduled(fixedDelay = 5000)
    public void sendHeartbeat() {

        WorkerHeartbeatEvent event = new WorkerHeartbeatEvent(
                UUID.randomUUID(),
                workerId,
                host,
                port,
                WorkerStatus.IDLE,
                LocalDateTime.now()
        );

        kafkaProducer.send("worker-heartbeat", workerId.toString(), event);

        log.debug("Heartbeat sent for worker: {}", workerId);
    }

    public UUID getWorkerId() {
        return workerId;
    }
}