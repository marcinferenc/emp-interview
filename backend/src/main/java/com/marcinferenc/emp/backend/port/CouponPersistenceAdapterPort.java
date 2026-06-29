package com.marcinferenc.emp.backend.port;

import com.marcinferenc.emp.backend.adapter.persistence.service.CouponPersistenceService;
import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponDO;
import com.marcinferenc.emp.backend.domain.model.CouponResponseStatusDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponPersistenceAdapterPort implements CouponPersistencePort {
    private final CouponPersistenceService couponPersistenceService;

    @Override
    public CouponCreationResponseDO create(CouponCreationRequestDO couponCreationRequestDO) {
        try {
            return couponPersistenceService.create(couponCreationRequestDO);
        } catch (DataIntegrityViolationException e) {
            return CouponCreationResponseDO.builder()
                .status(CouponResponseStatusDO.FAILURE)
                .message(String.format("Coupon NOT created, already exists: %s", couponCreationRequestDO.getCouponCode()))
                .build();
        }
    }

    @Override
    public CouponClaimResponseDO claim(CouponClaimRequestDO couponClaimRequestDO) {
        return couponPersistenceService.claim(couponClaimRequestDO);
    }

    @Override
    public Optional<CouponDO> find(String couponCode, String countryCode) {
        return Optional.ofNullable(couponPersistenceService.find(couponCode, countryCode));
    }
}