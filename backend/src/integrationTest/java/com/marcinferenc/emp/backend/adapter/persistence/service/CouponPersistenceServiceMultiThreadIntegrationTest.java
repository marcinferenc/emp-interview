package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.CouponBackendApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(
    classes = CouponBackendApplication.class,
    properties = {
        "spring.datasource.url=${POSTGRES_URL}",
        "spring.datasource.username=${POSTGRES_USER}",
        "spring.datasource.password=${POSTGRES_PASSWORD}"
    }
)
@Slf4j
@Import(CouponCreationTestComponent.class)
public class CouponPersistenceServiceMultiThreadIntegrationTest {
    @Test void shouldCreateCouponsConcurrently() {

    }
}
