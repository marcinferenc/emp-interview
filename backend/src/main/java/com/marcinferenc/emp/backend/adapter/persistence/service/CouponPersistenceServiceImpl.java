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
import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.rest.ErrorCode;
import com.marcinferenc.emp.backend.rest.model.CouponException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

        final String couponCode = couponClaimRequestPO.getCouponCode();
        Optional<CouponBO> couponByCode = couponRepository.findByCouponCode(couponCode);
        AtomicReference<CouponClaimResponsePO> result = new AtomicReference<>();

        couponByCode.ifPresentOrElse(
            couponBO -> {
                log.info("Found coupon: {}", couponBO);
                Integer currentClaimCount = couponBO.getClaimCount();

                result.set(CouponClaimResponsePO.builder()
                    .message(String.format("Coupon claimed OK: %d -> %d", currentClaimCount, ++currentClaimCount))
                    .build());

                if (currentClaimCount >= couponBO.getClaimLimitCount()) {
                    throwClaimLimitExceeded(couponBO);
                }
                CouponBO updatedCoupon = deepCopyWithClaimCountIncreased(couponBO);
                couponRepository.save(updatedCoupon);

            },
            () -> throwCouponNotFound(couponCode));

        return couponClaimResponsePersistenceConverter.toDomainObject(result.get());
    }

    protected List<CouponBO> findAll() {
        List<CouponBO> allCouponBOs = couponRepository.findAll();
        if (log.isDebugEnabled()) {
            log.debug("Found coupons: {}", allCouponBOs);
        }

        return allCouponBOs;
    }
    //------------------------------------------------------

    private void throwCouponNotFound(String couponByCode) {
        throw new CouponException(
            ErrorCode.COUPON_NOT_FOUND,
            String.format("coupon not found: %s", couponByCode));
    }

    private void throwClaimLimitExceeded(CouponBO couponBO) {
        throw new CouponException(
            ErrorCode.CLAIM_LIMIT_EXCEEDED,
            String.format("coupon claim limit exceeded, %s", couponBO)
        );
    }

        private CouponBO deepCopyWithClaimCountIncreased(CouponBO couponBO) {
        return CouponBO.builder()
            .id(couponBO.getId())
            .couponCode(couponBO.getCouponCode())
            .countryCode(couponBO.getCountryCode())
            .claimLimitCount(couponBO.getClaimLimitCount())
            .claimCount(couponBO.getClaimCount() + 1)
            .createdAt(couponBO.getCreatedAt())
            .build();
    }
}
