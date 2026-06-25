package com.vortex.scheduler.business.services;

import com.vortex.scheduler.business.dtos.ScheduleDto;
import com.vortex.scheduler.business.ports.IScheduledJobRepository;
import com.vortex.scheduler.core.entities.ScheduledJob;
import com.vortex.scheduler.core.exceptions.SchedulerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final IScheduledJobRepository jobRepository;
    private final Scheduler scheduler;

    public ScheduledJob createSchedule(ScheduleDto dto) throws SchedulerException {
        ScheduledJob job = new ScheduledJob(
                UUID.randomUUID(),
                dto.name(),
                dto.type(),
                dto.priority(),
                dto.cronExpression(),
                dto.maxRetries(),
                dto.timeoutSeconds(),
                dto.payload(),
                true
        );

        ScheduledJob saved = jobRepository.save(job);

        try {
            JobDetail jobDetail = JobBuilder.newJob(com.vortex.scheduler.infra.jobs.VortexJob.class)
                    .withIdentity(saved.getName(), "vortex")
                    .usingJobData("jobName", saved.getName())
                    .usingJobData("jobType", saved.getType().name())
                    .usingJobData("jobPriority", saved.getPriority().name())
                    .usingJobData("maxRetries", saved.getMaxRetries())
                    .usingJobData("timeoutSeconds", saved.getTimeoutSeconds())
                    .usingJobData("payload", saved.getPayload())
                    .storeDurably()
                    .build();

            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(saved.getName() + "-trigger", "vortex")
                    .withSchedule(CronScheduleBuilder.cronSchedule(saved.getCronExpression()))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Schedule created: {}", saved.getName());

        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException("Failed to schedule job: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return saved;
    }

    public List<ScheduledJob> getActiveSchedules() {
        return jobRepository.findAllActive();
    }

    public void deleteSchedule(UUID id) {
        ScheduledJob job = jobRepository.findById(id)
                .orElseThrow(() -> new SchedulerException("Schedule not found: " + id, HttpStatus.NOT_FOUND));

        try {
            scheduler.deleteJob(JobKey.jobKey(job.getName(), "vortex"));
            jobRepository.delete(id);
            log.info("Schedule deleted: {}", job.getName());
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException("Failed to delete job: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}