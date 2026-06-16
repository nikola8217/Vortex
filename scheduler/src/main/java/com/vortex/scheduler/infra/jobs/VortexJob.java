package com.vortex.scheduler.infra.jobs;

import com.vortex.scheduler.business.services.TaskQueueService;
import com.vortex.shared.entities.Task;
import com.vortex.shared.enums.TaskPriority;
import com.vortex.shared.enums.TaskStatus;
import com.vortex.shared.enums.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class VortexJob implements Job {

    @Autowired
    private TaskQueueService taskQueueService;

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap data = context.getJobDetail().getJobDataMap();

        Task task = Task.builder()
                .id(UUID.randomUUID())
                .name(data.getString("jobName"))
                .type(TaskType.valueOf(data.getString("jobType")))
                .priority(TaskPriority.valueOf(data.getString("jobPriority")))
                .status(TaskStatus.PENDING)
                .maxRetries(data.getInt("maxRetries"))
                .retryCount(0)
                .timeoutSeconds(data.getInt("timeoutSeconds"))
                .cronExpression(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .scheduledAt(LocalDateTime.now())
                .build();

        taskQueueService.enqueue(task);
        log.info("Quartz triggered task: {}", task.getName());
    }
}