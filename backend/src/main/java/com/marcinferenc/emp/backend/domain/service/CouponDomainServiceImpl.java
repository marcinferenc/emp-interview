package com.marcinferenc.emp.backend.domain.service;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.port.CouponPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

        return CouponCreationResponseDO.builder()
            .message("coupon created OK")
            .build();
    }

    private void validateCouponCodeAllLowerCaseChars(String couponCode) {
        if (couponCode != null && couponCode.chars().anyMatch(Character::isUpperCase)) {
            throw new IllegalArgumentException("Coupon code must not contain upper case letters");
        }
    }
}
