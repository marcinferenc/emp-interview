package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.CouponBackendApplication;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
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
    private static final int COUPON_COUNT = 100;
    private static final int COUPON_CODE_LENGTH = 2;
    private static final int COUPON_CLAIM_LIMIT_COUNT = 50;
    private static final String COUNTRY_CODE = "PL";

    @Autowired private CouponPersistenceServiceImpl couponPersistenceService;
    @Autowired private CouponRepository couponRepository;
    @Autowired private DataSource dataSource;

    @BeforeEach
    void setUp() {
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
    void createCouponsAndClaimThem() {
        createCoupons();

        List<CouponBO> allCoupons = couponRepository.findAll();
        assertThat(allCoupons).size().isEqualTo(COUPON_COUNT);
        assertAllCouponsClaimAmount(allCoupons, 0);

        claimCoupons(allCoupons);

        List<CouponBO> allCouponsAfterClaim = couponPersistenceService.findAll();
        assertAllCouponsClaimAmount(allCouponsAfterClaim, COUPON_CLAIM_LIMIT_COUNT);
    }

    private void assertAllCouponsClaimAmount(List<CouponBO> allCouponsAfterClaim, int couponClaimLimitCount) {
        for (CouponBO coupon : allCouponsAfterClaim) {
            assertThat(coupon.getClaimCount()).isEqualTo(couponClaimLimitCount);
        }
    }

    private void claimCoupons(List<CouponBO> allCoupons) {
        for (CouponBO coupon : allCoupons) {
            for (int i = 0; i < coupon.getClaimLimitCount(); i++) {
                couponPersistenceService.claim(
                    CouponClaimRequestDO.builder()
                        .couponCode(coupon.getCouponCode())
                        .userEmailId("user@server.com")
                        .build());
            }
        }
    }

    private Set<CouponCreationRequestDO> generateCouponCreationRequestDO() {
        final Set<String> couponCodes = new LinkedHashSet<>();
        final Set<CouponCreationRequestDO> couponCreationRequestDOSet = new LinkedHashSet<>();

        while (couponCodes.size() < COUPON_COUNT) {
            couponCodes.add(RandomStringUtils.random(COUPON_CODE_LENGTH, true, true).toUpperCase());
        }

        log.info("coupon codes: {}", couponCodes);

        for (String couponCode : couponCodes) {
            CouponCreationRequestDO couponCreationRequestDO = CouponCreationRequestDO.builder()
                .couponCode(couponCode)
                .countryCode(COUNTRY_CODE)
                .claimCount(0)
                .claimLimitCount(COUPON_CLAIM_LIMIT_COUNT)
                .createdAt(Instant.now())
                .build();
            couponCreationRequestDOSet.add(couponCreationRequestDO);
        }

        return couponCreationRequestDOSet;
    }

    private void createCoupons() {
        Set<CouponCreationRequestDO> couponCreationRequestDOS = generateCouponCreationRequestDO();
        for (CouponCreationRequestDO creationRequestDO : couponCreationRequestDOS) {
            couponPersistenceService.create(creationRequestDO);
        }
    }
}
