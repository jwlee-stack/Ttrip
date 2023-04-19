package com.ttrip.demo.batch.JobTest;

import com.ttrip.demo.batch.config.BatchDataSourceConfig;
import com.ttrip.demo.batch.config.TtripDataSourceConfig;
import com.ttrip.demo.batch.config.TestBatchConfig;
import com.ttrip.demo.batch.job.HardDeleteConfig;
import org.junit.runner.RunWith;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HardDeleteConfig.class, TestBatchConfig.class, BatchDataSourceConfig.class, TtripDataSourceConfig.class})
@SpringBatchTest
@ActiveProfiles("batchtest")
public class HardDeleteTotalTest {

}
