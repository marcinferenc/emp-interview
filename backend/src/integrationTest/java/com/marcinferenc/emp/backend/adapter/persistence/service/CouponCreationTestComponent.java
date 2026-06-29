package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.marcinferenc.emp.backend.adapter.persistence.service.TestConfig.COUNTRY_CODE;
import static com.marcinferenc.emp.backend.adapter.persistence.service.TestConfig.COUPON_CLAIM_LIMIT_COUNT;
import static com.marcinferenc.emp.backend.adapter.persistence.service.TestConfig.COUPON_CODE_LENGTH;
import static com.marcinferenc.emp.backend.adapter.persistence.service.TestConfig.COUPON_COUNT;

@TestComponent
@Slf4j
public class CouponCreationTestComponent {

    @Autowired
    private CouponPersistenceServiceImpl couponPersistenceService;

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

    Set<CouponCreationResponseDO> createCoupons() {
        Set<CouponCreationResponseDO> couponCreationResponseDOSet = new LinkedHashSet<>();

        Set<CouponCreationRequestDO> couponCreationRequestDOS = generateCouponCreationRequestDO();
        for (CouponCreationRequestDO creationRequestDO : couponCreationRequestDOS) {
            couponCreationResponseDOSet.add(couponPersistenceService.create(creationRequestDO));
        }

        return couponCreationResponseDOSet;
    }

    void claimCoupons(List<CouponBO> allCoupons) {
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
}
