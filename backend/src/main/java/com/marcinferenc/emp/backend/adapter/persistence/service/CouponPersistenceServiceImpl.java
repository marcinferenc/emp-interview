package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.adapter.persistence.converter.CouponClaimRequestPersistenceConverter;
import com.marcinferenc.emp.backend.adapter.persistence.converter.CouponClaimResponsePersistenceConverter;
import com.marcinferenc.emp.backend.adapter.persistence.converter.CouponCreationRequestPersistenceConverter;
import com.marcinferenc.emp.backend.adapter.persistence.converter.CouponCreationResponsePersistenceConverter;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponClaimRequestPO;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponClaimResponsePO;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationRequestPO;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationResponsePO;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponResponseStatusPO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponDO;
import com.marcinferenc.emp.backend.rest.ErrorCode;
import com.marcinferenc.emp.backend.rest.model.CouponException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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

        CouponBO persistedCoupon = null;
        CouponCreationResponsePO couponCreationResponsePO = null;

        persistedCoupon = couponRepository.save(couponBO);
        log.info("Saved coupon: {}", persistedCoupon);
        couponCreationResponsePO = CouponCreationResponsePO.builder()
            .status(CouponResponseStatusPO.SUCCESS)
            .message(String.format("Coupon created OK: %s", persistedCoupon))
            .build();

        return couponCreationResponsePersistenceConverter.toDomainObject(couponCreationResponsePO);
    }

    @Override
    public CouponClaimResponseDO claim(CouponClaimRequestDO couponClaimRequestDO) {
        try {
            CouponClaimRequestPO couponClaimRequestPO = couponClaimRequestPersistenceConverter.toPersistenceObject(couponClaimRequestDO);

            final String couponCode = couponClaimRequestPO.getCouponCode();
            int updatedRows = couponRepository.incrementClaimCountIfBelowLimit(couponCode);
            CouponBO couponBO = couponRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> createCouponNotFound(couponCode));

            log.trace("Found coupon: {}", couponBO);

            if (updatedRows == 0) {
                throwClaimLimitExceeded(couponBO);
            }

            Integer updatedClaimCount = couponBO.getClaimCount();
            Integer claimLimitCount = couponBO.getClaimLimitCount();
            CouponClaimResponsePO result = CouponClaimResponsePO.builder()
                .status(CouponResponseStatusPO.SUCCESS)
                .userEmailId(couponClaimRequestPO.getUserEmailId())
                .couponCode(couponBO.getCouponCode())
                .timestamp(Instant.now())
                .message(String.format("Coupon `%s` claimed OK: %d -> %d / %d",
                    couponCode,
                    updatedClaimCount - 1,
                    updatedClaimCount,
                    claimLimitCount))
                .build();

            return couponClaimResponsePersistenceConverter.toDomainObject(result);
        } catch (Exception e) {
            log.error("Error while claiming coupon {}", couponClaimRequestDO.getCouponCode(), e);
            throw e;
        }
    }

    @Override
    public CouponDO find(String couponCode, String countryCode) {
        return null;
    }

    protected List<CouponBO> findAll() {
        List<CouponBO> allCouponBOs = couponRepository.findAll();
        if (log.isDebugEnabled()) {
            log.debug("Found coupons: {}", allCouponBOs);
        }

        return allCouponBOs;
    }
    //------------------------------------------------------

    private CouponException createCouponNotFound(String couponByCode) {
        return new CouponException(
            ErrorCode.COUPON_NOT_FOUND,
            String.format("coupon not found: %s", couponByCode));
    }

    private void throwClaimLimitExceeded(CouponBO couponBO) {
        throw new CouponException(
            ErrorCode.CLAIM_LIMIT_EXCEEDED,
            String.format("coupon claim limit exceeded, %s", couponBO)
        );
    }

}
