package com.marcinferenc.emp.backend;

import com.marcinferenc.emp.backend.adapter.persistence.service.CouponRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude="
			+ "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,"
			+ "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration"
})
class CouponBackendApplicationTests {

	@Test
	void contextLoads() {
	}

	@TestConfiguration
	static class MockPersistenceConfiguration {

		@Bean
		CouponRepository couponRepository() {
			return Mockito.mock(CouponRepository.class);
		}
	}

}
