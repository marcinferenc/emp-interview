package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponDO;
import com.marcinferenc.emp.backend.domain.model.CouponResponseStatusDO;
import com.marcinferenc.emp.backend.rest.ErrorCode;
import com.marcinferenc.emp.backend.rest.model.CouponException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponPersistenceServiceImpl implements CouponPersistenceService {
    private final CouponRepository couponRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public CouponCreationResponseDO create(CouponCreationRequestDO couponCreationRequestDO) {
        CouponBO couponBO = CouponBO.builder()
            .couponCode(couponCreationRequestDO.getCountryCode())
            .couponCode(couponCreationRequestDO.getCouponCode())
            .countryCode(couponCreationRequestDO.getCountryCode())
            .claimLimitCount(couponCreationRequestDO.getClaimLimitCount())
            .claimCount(couponCreationRequestDO.getClaimCount())
            .createdAt(couponCreationRequestDO.getCreatedAt())
            .build();

        CouponCreationResponseDO couponCreationResponseDO = null;
        CouponBO persistedCoupon = transactionTemplate.execute(status -> {
            couponRepository.save(couponBO);
            couponRepository.flush();
            return couponBO;
        });

        log.info("persisted coupon: {}", persistedCoupon);
        couponCreationResponseDO = CouponCreationResponseDO.builder()
            .status(CouponResponseStatusDO.SUCCESS)
            .message(String.format("Coupon created OK: %s", persistedCoupon))
            .build();

        return couponCreationResponseDO;
    }

    @Override
    public CouponClaimResponseDO claim(CouponClaimRequestDO couponClaimRequestDO) {
        try {
            final String requestedCouponCode = couponClaimRequestDO.getCouponCode();
            String userEmailId = couponClaimRequestDO.getUserEmailId();

            CouponBO couponBO = transactionTemplate.execute(
                status -> performClaimUpdateTransaction(couponClaimRequestDO, requestedCouponCode));

            Integer updatedClaimCount = couponBO.getClaimCount();
            Integer claimLimitCount = couponBO.getClaimLimitCount();
            String retrievedCountryCode = couponBO.getCountryCode();

            CouponClaimResponseDO result = createClaimResult(
                couponBO,
                requestedCouponCode,
                retrievedCountryCode,
                userEmailId,
                updatedClaimCount,
                claimLimitCount);

            return result;

        } catch (Exception e) {
            log.error("Error while claiming coupon {}", couponClaimRequestDO.getCouponCode(), e);
            throw e;
        }
    }

    private CouponClaimResponseDO createClaimResult(CouponBO couponBO,
                                                    String requestedCouponCode,
                                                    String retrievedCountryCode,
                                                    String userEmailId,
                                                    Integer updatedClaimCount,
                                                    Integer claimLimitCount) {
        return CouponClaimResponseDO.builder()
            .status(CouponResponseStatusDO.SUCCESS)
            .userEmailId(userEmailId)
            .couponCode(couponBO.getCouponCode())
            .timestamp(Instant.now())
            .message(String.format("Coupon code: %s, countryCode: %s claimed OK: %d -> %d / %d",
                requestedCouponCode,
                retrievedCountryCode,
                updatedClaimCount - 1,
                updatedClaimCount,
                claimLimitCount))
            .build();
    }

    private CouponBO performClaimUpdateTransaction(CouponClaimRequestDO couponClaimRequestDO, String couponCode) {
        int updatedRows = couponRepository.incrementClaimCountIfBelowLimit(couponCode);
        couponRepository.flush();
        CouponBO couponBO = couponRepository.findByCouponCode(couponCode)
            .orElseThrow(() -> createCouponNotFoundException(couponClaimRequestDO));

        log.trace("Found coupon: {}", couponBO);

        if (updatedRows == 0) {
            throwClaimLimitExceeded(couponBO);
        }

        return couponBO;
    }

    @Override
    public CouponDO find(String couponCode, String countryCode) {
        return couponRepository.findByCouponCodeAndCountryCode(couponCode, countryCode)
            .map(this::toDomainObject)
            .orElse(null);
    }

    protected List<CouponBO> findAll() {
        List<CouponBO> allCouponBOs = couponRepository.findAll();
        if (log.isDebugEnabled()) {
            log.debug("Found coupons: {}", allCouponBOs);
        }

        return allCouponBOs;
    }

    //------------------------------------------------------
    private CouponException createCouponNotFoundException(CouponClaimRequestDO couponClaimRequestDO) {
        String couponCode = couponClaimRequestDO.getCouponCode();
        String countryCode = couponClaimRequestDO.getCountryCode();

        return new CouponException(
            ErrorCode.COUPON_NOT_FOUND,
            String.format("coupon not found. couponCode: %s, countryCode: %s", couponCode, countryCode));
    }

    private void throwClaimLimitExceeded(CouponBO couponBO) {
        throw new CouponException(
            ErrorCode.CLAIM_LIMIT_EXCEEDED,
            String.format("coupon claim limit exceeded, %s", couponBO)
        );
    }

    private CouponDO toDomainObject(CouponBO couponBO) {
        return CouponDO.builder()
            .couponCode(couponBO.getCouponCode())
            .countryCode(couponBO.getCountryCode())
            .build();
    }
}
