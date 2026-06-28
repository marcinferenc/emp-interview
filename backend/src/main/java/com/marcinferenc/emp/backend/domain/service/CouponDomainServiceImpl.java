package com.marcinferenc.emp.backend.domain.service;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.port.CouponPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponDomainServiceImpl implements CouponDomainService {
    private final CouponPersistencePort couponPersistencePort;

    @Override
    public CouponClaimResponseDO claim(CouponClaimRequestDO couponClaimRequestDO) {
        validateCouponCodeAllLowerCaseChars(couponClaimRequestDO.getCouponCode());

        return CouponClaimResponseDO.builder()
            .message("coupon claimed OK")
            .build();
    }

    @Override
    public CouponCreationResponseDO create(CouponCreationRequestDO couponCreationRequestDO) {
        validateCouponCodeAllLowerCaseChars(couponCreationRequestDO.getCouponCode());
        validateBeforeDataPopulation(couponCreationRequestDO);

        couponCreationRequestDO.setClaimCount(0);
        couponCreationRequestDO.setCreatedAt(Instant.now());

        return couponPersistencePort.create(couponCreationRequestDO);
    }

    private void validateBeforeDataPopulation(CouponCreationRequestDO couponCreationRequestDO) {
        Validate.isTrue(couponCreationRequestDO.getClaimCount() == null);
        Validate.isTrue(couponCreationRequestDO.getCreatedAt() == null);

    }

    private void validateCouponCodeAllLowerCaseChars(String couponCode) {
        if (couponCode != null && couponCode.chars().anyMatch(Character::isUpperCase)) {
            throw new IllegalArgumentException("Coupon code must not contain upper case letters");
        }
    }
}
