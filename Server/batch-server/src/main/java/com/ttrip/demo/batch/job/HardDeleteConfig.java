package com.ttrip.demo.batch.job;

import com.ttrip.demo.batch.config.TtripDataSourceConfig;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
@RequiredArgsConstructor
public class HardDeleteConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final JobRepository jobRepository;
    private final TtripDataSourceConfig ttripDataSourceConfig;
    private static Logger logger = LogManager.getLogger(HardDeleteConfig.class);
    private int chunkSize = 100;

    @PersistenceContext(unitName = "ttripEntityManager")
    private EntityManager em;


}