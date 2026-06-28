package com.vortex.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
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
public class VortexApplication {

    public static void main(String[] args) {
        SpringApplication.run(VortexApplication.class, args);
    }
}