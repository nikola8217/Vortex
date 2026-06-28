package com.vortex.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
@ComponentScan(basePackages = {
        "com.vortex.app",
        "com.vortex.shared",
        "com.vortex.scheduler",
        "com.vortex.worker",
        "com.vortex.task",
        "com.vortex.observability"
})
@EnableJpaRepositories(basePackages = {
        "com.vortex.scheduler.infra.repositories",
        "com.vortex.worker.infra.repositories",
        "com.vortex.task.infra.repositories"
})
@EntityScan(basePackages = {
        "com.vortex.scheduler.infra.models",
        "com.vortex.worker.infra.models",
        "com.vortex.task.infra.models"
})
public class VortexApplication {
    public static void main(String[] args) {
        SpringApplication.run(VortexApplication.class, args);
    }
}