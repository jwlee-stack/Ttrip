package com.ttrip.demo.batch.scheduler;

import com.ttrip.demo.batch.job.HardDeleteConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final HardDeleteConfig hardDeleteConfig;
    private static Logger logger = LogManager.getLogger(JobScheduler.class);


}
