package com.ttrip.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = TtripApplication.class)
@ActiveProfiles("test")
class TtripApplicationTests {

	@Test
	void contextLoads() {
	}

}
