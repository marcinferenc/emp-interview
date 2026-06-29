package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.CouponBackendApplication;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.rest.model.CouponException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
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
import static com.marcinferenc.emp.backend.adapter.persistence.service.TestConfig.COUPON_COUNT;
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
        Set<CouponCreationResponseDO> couponCreationResponseDOSet = createCouponsConcurrently(couponCodes);

        List<CouponBO> allCoupons = couponRepository.findAll();
        assertThat(allCoupons).hasSize(couponCreationResponseDOSet.size());
        assertAllCouponsClaimAmount(allCoupons, 0);

        Set<CouponClaimResponseDO> couponClaimResponseDOS = runConcurrently(createFullClaimTasks(allCoupons));
        log.info("Coupon claim responses: {}", couponClaimResponseDOS);

        List<CouponBO> allCouponsAfterClaim = couponPersistenceService.findAll();
        assertAllCouponsClaimAmount(allCouponsAfterClaim, COUPON_CLAIM_LIMIT_COUNT);
    }

    @Test
    void shouldThrowWhenClaimLimitExceeded() throws Exception {
        Set<String> couponCodes = couponCreationTestComponent.generateCouponCodes();
        createCouponsConcurrently(couponCodes);
        List<CouponBO> allCoupons = couponRepository.findAll();

        Set<CouponClaimResponseDO> couponClaimResponses = null;
        try {
            couponClaimResponses = runConcurrently(createFullClaimTasks(allCoupons, 1));
        } catch (CouponException e) {
            //expected once per coupon code
            log.trace("Expected exception - coupon claim over limit", e);
        }

        //when
        assertThat(couponClaimResponses).hasSize(COUPON_CLAIM_LIMIT_COUNT * COUPON_COUNT);
        assertAllCouponsClaimAmount(couponRepository.findAll(), COUPON_CLAIM_LIMIT_COUNT);
    }

    private @NonNull Set<CouponCreationResponseDO> createCouponsConcurrently(Set<String> couponCodes) throws Exception {
        Set<CouponCreationResponseDO> couponCreationResponseDOSet = runConcurrently(
            couponCodes.stream()
                .<Callable<CouponCreationResponseDO>>map(
                    couponCode ->
                        () ->
                            couponCreationTestComponent.createCoupons(couponCode))
                .toList());
        if (log.isTraceEnabled()) {
            log.trace("Coupon creation responses: {}", couponCreationResponseDOSet);
        }

        return couponCreationResponseDOSet;
    }

    private <T> Set<T> runConcurrently(List<Callable<T>> tasks) throws Exception {
        try (ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            List<Future<T>> futures = executorService.invokeAll(tasks, TestConfig.TIMEOUT_SECONDS, TestConfig.TIMEOUT_TIME_UNIT);
            return collectResults(futures);
        }
    }

    private List<Callable<CouponClaimResponseDO>> createFullClaimTasks(List<CouponBO> allCoupons) {
        return this.createFullClaimTasks(allCoupons, 0);
    }

    private List<Callable<CouponClaimResponseDO>> createFullClaimTasks(List<CouponBO> allCoupons, int extraClaimCount) {
        Validate.isTrue(extraClaimCount >= 0);

        List<Callable<CouponClaimResponseDO>> tasks = new ArrayList<>();
        for (CouponBO coupon : allCoupons) {
            for (int i = 0; i < coupon.getClaimLimitCount() + extraClaimCount; i++) {
                tasks.add(() -> couponCreationTestComponent.claimCoupon(coupon));
            }
        }
        return tasks;
    }

    private <T> Set<T> collectResults(List<Future<T>> futures) throws InterruptedException, ExecutionException, TimeoutException {
        Set<T> results = new LinkedHashSet<>();
        for (Future<T> future : futures) {
            try {
                results.add(future.get(TestConfig.TIMEOUT_SECONDS, TestConfig.TIMEOUT_TIME_UNIT));
            } catch (ExecutionException e) {
                log.info("Exception while claiming coupon", e);
            }
        }
        return results;
    }

    private void assertAllCouponsClaimAmount(List<CouponBO> allCouponsAfterClaim, int couponClaimLimitCount) {
        for (CouponBO coupon : allCouponsAfterClaim) {
            assertThat(coupon.getClaimCount()).isEqualTo(couponClaimLimitCount);
        }
    }
}
