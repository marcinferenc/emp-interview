package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.CouponBackendApplication;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Set;

import static com.marcinferenc.emp.backend.adapter.persistence.service.TestConfig.COUPON_CLAIM_LIMIT_COUNT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = CouponBackendApplication.class,
    properties = {
        "spring.datasource.url=${POSTGRES_URL}",
        "spring.datasource.username=${POSTGRES_USER}",
        "spring.datasource.password=${POSTGRES_PASSWORD}"
    }
)
@Import(CouponCreationTestComponent.class)
@Slf4j
class CouponPersistenceServiceSingleThreadIntegrationTest {
    @Autowired private CouponPersistenceServiceImpl couponPersistenceService;
    @Autowired private CouponRepository couponRepository;
    @Autowired private DataSource dataSource;
    @Autowired private CouponCreationTestComponent couponCreationTestComponent;

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
        Set<CouponCreationResponseDO> couponCreationResponseDOSet = couponCreationTestComponent.createCoupons();

        List<CouponBO> allCoupons = couponRepository.findAll();
        assertThat(allCoupons).size().isEqualTo(couponCreationResponseDOSet.size());
        assertAllCouponsClaimAmount(allCoupons, 0);

        couponCreationTestComponent.claimCoupons(allCoupons);

        List<CouponBO> allCouponsAfterClaim = couponPersistenceService.findAll();
        assertAllCouponsClaimAmount(allCouponsAfterClaim, COUPON_CLAIM_LIMIT_COUNT);
    }

    private void assertAllCouponsClaimAmount(List<CouponBO> allCouponsAfterClaim, int couponClaimLimitCount) {
        for (CouponBO coupon : allCouponsAfterClaim) {
            assertThat(coupon.getClaimCount()).isEqualTo(couponClaimLimitCount);
        }
    }
}
