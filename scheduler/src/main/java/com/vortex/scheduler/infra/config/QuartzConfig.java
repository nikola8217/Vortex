package com.vortex.scheduler.infra.config;

import com.vortex.scheduler.business.ports.IScheduledJobRepository;
import com.vortex.scheduler.core.entities.ScheduledJob;
import com.vortex.scheduler.infra.jobs.VortexJob;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final IScheduledJobRepository jobRepository;
    private final Scheduler scheduler;

    @PostConstruct
    public void scheduleJobs() throws SchedulerException {
        List<ScheduledJob> activeJobs = jobRepository.findAllActive();

        for (ScheduledJob job : activeJobs) {
            JobDetail jobDetail = JobBuilder.newJob(VortexJob.class)
                    .withIdentity(job.getName(), "vortex")
                    .usingJobData("jobName", job.getName())
                    .usingJobData("jobType", job.getType().name())
                    .usingJobData("jobPriority", job.getPriority().name())
                    .usingJobData("maxRetries", job.getMaxRetries())
                    .usingJobData("timeoutSeconds", job.getTimeoutSeconds())
                    .usingJobData("payload", job.getPayload())
                    .storeDurably()
                    .build();

            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(job.getName() + "-trigger", "vortex")
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression()))
                    .build();

            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, trigger);
                log.info("Scheduled job: {}", job.getName());
            }
        }
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        return new SpringBeanJobFactory();
    }
}