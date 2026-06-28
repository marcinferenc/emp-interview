package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.adapter.persistence.converter.CouponClaimRequestPersistenceConverter;
import com.marcinferenc.emp.backend.adapter.persistence.converter.CouponClaimResponsePersistenceConverter;
import com.marcinferenc.emp.backend.adapter.persistence.converter.CouponCreationRequestPersistenceConverter;
import com.marcinferenc.emp.backend.adapter.persistence.converter.CouponCreationResponsePersistenceConverter;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponClaimRequestPO;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationRequestPO;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationResponsePO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CouponPersistenceServiceImpl implements CouponPersistenceService {
    private final CouponRepository couponRepository;

    private final CouponCreationRequestPersistenceConverter couponCreationRequestPersistenceConverter;
    private final CouponCreationResponsePersistenceConverter couponCreationResponsePersistenceConverter;

    private final CouponClaimRequestPersistenceConverter couponClaimRequestPersistenceConverter;
    private final CouponClaimResponsePersistenceConverter couponClaimResponsePersistenceConverter;

    @Override
    public CouponCreationResponseDO create(CouponCreationRequestDO couponCreationRequestDO) {
        CouponCreationRequestPO couponCreationRequestPO = couponCreationRequestPersistenceConverter.toPersistenceObject(couponCreationRequestDO);

        CouponBO couponBO = CouponBO.builder()
            .couponCode(couponCreationRequestPO.getCouponCode())
            .countryCode(couponCreationRequestPO.getCountryCode())
            .claimLimitCount(couponCreationRequestPO.getClaimLimitCount())
            .claimCount(couponCreationRequestPO.getClaimCount())
            .createdAt(couponCreationRequestPO.getCreatedAt())
            .build();

        CouponBO persistedCoupon = couponRepository.save(couponBO);
        log.info("Saved coupon: {}", persistedCoupon);

        CouponCreationResponsePO couponCreationResponsePO = CouponCreationResponsePO.builder()
            .message(String.format("Coupon created OK: %s", persistedCoupon))
            .build();

        return couponCreationResponsePersistenceConverter.toDomainObject(couponCreationResponsePO);
    }

    @Override
    public CouponClaimResponseDO claim(CouponClaimRequestDO couponClaimRequestDO) {
        CouponClaimRequestPO couponClaimRequestPO = couponClaimRequestPersistenceConverter.toPersistenceObject(couponClaimRequestDO);

        

        return null;
    }
}
