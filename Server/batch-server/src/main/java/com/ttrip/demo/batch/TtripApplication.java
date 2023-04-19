package com.ttrip.demo.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {"com.ttrip.demo.batch", "com.ttrip.core"})
@ComponentScan("com.ttrip.core")
@ComponentScan("com.ttrip.demo.batch")
@EnableScheduling
@EnableBatchProcessing
public class TtripApplication {

	public static void main(String[] args) {
		SpringApplication.run(TtripApplication.class, args);
	}
}
