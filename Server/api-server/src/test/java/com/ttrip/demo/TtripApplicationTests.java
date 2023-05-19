package com.ttrip.demo;

import com.ttrip.core.TtripApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TtripApplication.class)
@ActiveProfiles("apitest")
class TtripApplicationTests {
	@Test
	void contextLoads() {
	}

}
