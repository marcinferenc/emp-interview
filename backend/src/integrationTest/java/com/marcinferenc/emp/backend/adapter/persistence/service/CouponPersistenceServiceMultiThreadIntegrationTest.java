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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

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
@Slf4j
@Import(CouponCreationTestComponent.class)
public class CouponPersistenceServiceMultiThreadIntegrationTest {
    @Autowired private CouponCreationTestComponent couponCreationTestComponent;
    @Autowired private CouponPersistenceServiceImpl couponPersistenceService;
    @Autowired private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        couponRepository.deleteAll();
    }

    @Test
    void shouldCreateAndClaimCouponsConcurrently() throws Exception {
        Set<String> couponCodes = couponCreationTestComponent.generateCouponCodes();

        Set<CouponCreationResponseDO> couponCreationResponseDOSet = runConcurrently(
            couponCodes.stream()
                .<Callable<CouponCreationResponseDO>>map(
                    couponCode ->
                        () ->
                            couponCreationTestComponent.createCoupons(couponCode))
                .toList());

        List<CouponBO> allCoupons = couponRepository.findAll();
        assertThat(allCoupons).hasSize(couponCreationResponseDOSet.size());
        assertAllCouponsClaimAmount(allCoupons, 0);

        runConcurrently(
            allCoupons.stream()
                .<Callable<Void>>map(
                    coupon ->
                        () -> {
                            couponCreationTestComponent.claimCoupons(coupon);
                            return null;
                })
                .toList());

        List<CouponBO> allCouponsAfterClaim = couponPersistenceService.findAll();
        assertAllCouponsClaimAmount(allCouponsAfterClaim, COUPON_CLAIM_LIMIT_COUNT);
    }

    private <T> Set<T> runConcurrently(List<Callable<T>> tasks) throws Exception {
        try (ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            List<Future<T>> futures = executorService.invokeAll(tasks, TestConfig.TIMEOUT_SECONDS, TestConfig.TIMEOUT_TIME_UNIT);
            return collectResults(futures);
        }
    }

    private <T> Set<T> collectResults(List<Future<T>> futures) throws InterruptedException, ExecutionException, TimeoutException {
        Set<T> results = new LinkedHashSet<>();
        for (Future<T> future : futures) {
            results.add(future.get(TestConfig.TIMEOUT_SECONDS, TestConfig.TIMEOUT_TIME_UNIT));
        }
        return results;
    }

    private void assertAllCouponsClaimAmount(List<CouponBO> allCouponsAfterClaim, int couponClaimLimitCount) {
        for (CouponBO coupon : allCouponsAfterClaim) {
            assertThat(coupon.getClaimCount()).isEqualTo(couponClaimLimitCount);
        }
    }
}
