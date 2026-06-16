package com.vortex.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
@ComponentScan(basePackages = {"com.vortex.worker", "com.vortex.shared"})
public class WorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }
}