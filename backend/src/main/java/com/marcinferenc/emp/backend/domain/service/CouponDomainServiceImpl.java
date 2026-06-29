package com.marcinferenc.emp.backend.domain.service;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponDO;
import com.marcinferenc.emp.backend.port.CouponPersistencePort;
import com.marcinferenc.emp.backend.port.IpInfoPort;
import com.marcinferenc.emp.backend.rest.ErrorCode;
import com.marcinferenc.emp.backend.rest.model.CouponException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponDomainServiceImpl implements CouponDomainService {
    private final CouponPersistencePort couponPersistencePort;
    private final IpInfoPort ipInfoPort;

    @Override
    public CouponClaimResponseDO claim(CouponClaimRequestDO couponClaimRequestDO) {
        String couponCode = couponClaimRequestDO.getCouponCode();
        String ipAddress = couponClaimRequestDO.getIpAddress();
        validateCouponCodeAllLowerCaseChars(couponCode);

        String countryCode = ipInfoPort.getCountryCode(ipAddress);
        couponClaimRequestDO.setCountryCode(countryCode);
        Optional<CouponDO> couponOptional = couponPersistencePort.find(couponCode, countryCode);
        log.info("Coupon found: {}", couponOptional);

        AtomicReference<CouponClaimResponseDO> couponClaimResponseDO = new AtomicReference<>();
        couponOptional.ifPresentOrElse(coupon -> {
            log.info("Coupon found: {}", coupon);
            couponClaimResponseDO.set(couponPersistencePort.claim(couponClaimRequestDO));
        }, () -> {
            log.info("Coupon not found");
            throw new CouponException(
                ErrorCode.COUPON_NOT_FOUND,
                String.format("coupon not found. couponCode: %s, countryCode: %s", couponCode, countryCode));
        });

        return couponClaimResponseDO.get();
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
