package com.ttrip;

import com.ttrip.core.TtripApplication;
import com.ttrip.demo.batch.config.TtripDataSourceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = {TtripApplication.class, TtripDataSourceConfig.class})
@ActiveProfiles("batchtest")
class TtripApplicationTests {

	@Test
	void contextLoads() {
	}

}
