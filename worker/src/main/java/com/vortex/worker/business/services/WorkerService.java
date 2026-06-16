package com.vortex.worker.business.services;

import com.vortex.shared.enums.WorkerStatus;
import com.vortex.worker.business.ports.IWorkerRepository;
import com.vortex.worker.core.entities.WorkerEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerService {

    private final IWorkerRepository workerRepository;
    private final HeartbeatService heartbeatService;

    public WorkerEntity getCurrentWorker() {
        UUID workerId = heartbeatService.getWorkerId();
        return workerRepository.findById(workerId)
                .orElseThrow(() -> new com.vortex.worker.core.exceptions.WorkerException(
                        "Worker not found: " + workerId,
                        org.springframework.http.HttpStatus.NOT_FOUND
                ));
    }

    public List<WorkerEntity> getAvailableWorkers() {
        return workerRepository.findByStatus(WorkerStatus.IDLE);
    }

    public void updateStatus(UUID workerId, WorkerStatus status) {
        workerRepository.findById(workerId).ifPresent(worker -> {
            WorkerEntity updated = new WorkerEntity(
                    worker.getId(),
                    worker.getHost(),
                    worker.getPort(),
                    status
            );
            workerRepository.save(updated);
            log.info("Worker {} status updated to {}", workerId, status);
        });
    }
}