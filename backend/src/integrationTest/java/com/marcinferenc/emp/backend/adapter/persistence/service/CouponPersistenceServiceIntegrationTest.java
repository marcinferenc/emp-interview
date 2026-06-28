package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.CouponBackendApplication;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = CouponBackendApplication.class,
    properties = {
        "spring.datasource.url=${POSTGRES_URL}",
        "spring.datasource.username=${POSTGRES_USER}",
        "spring.datasource.password=${POSTGRES_PASSWORD}"
    }
)
@Slf4j
class CouponPersistenceServiceIntegrationTest {
    private static final int COUPON_COUNT = 10;
    private static final int COUPON_CODE_LENGTH = 2;
    private static final int COUPON_CLAIM_LIMIT_COUNT = 50;
    private static final String COUNTRY_CODE = "PL";

    @Autowired private CouponPersistenceServiceImpl couponPersistenceService;
    @Autowired private CouponRepository couponRepository;
    @Autowired private DataSource dataSource;

    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
    }

    @Test
    void shouldStartSpringContextAndUseDevDatabase() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection.getCatalog()).isEqualTo("emp_coupons_dev");
        }

        assertThat(couponPersistenceService.findAll()).isEmpty();
    }

    @Test
    void createCoupons() {
        Set<String> couponCodes = new LinkedHashSet<>();
        while (couponCodes.size() < COUPON_COUNT) {
            couponCodes.add(RandomStringUtils.random(COUPON_CODE_LENGTH, true, true).toUpperCase());
        }

        log.info("coupon codes: {}", couponCodes);

        for (String couponCode : couponCodes) {
            CouponCreationRequestDO creationRequestDO = CouponCreationRequestDO.builder()
                .couponCode(couponCode)
                .countryCode(COUNTRY_CODE)
                .claimCount(0)
                .claimLimitCount(COUPON_CLAIM_LIMIT_COUNT)
                .createdAt(Instant.now())
                .build();
            couponPersistenceService.create(creationRequestDO);
        }
    }
}
