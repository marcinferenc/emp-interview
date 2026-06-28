package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.adapter.persistence.converter.CouponCreationRequestPersistenceConverter;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationRequestPO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponPersistenceServiceImpl implements CouponPersistenceService {
    private final CouponRepository couponRepository;

    private final CouponCreationRequestPersistenceConverter couponCreationRequestPersistenceConverter;
    private final CouponCreationRequestPersistenceConverter couponCreationResponsePersistenceConverter;

    @Override
    @Transactional
    public CouponCreationResponseDO create(CouponCreationRequestDO couponCreationRequestDO) {
        CouponCreationRequestPO couponCreationRequestPO = couponCreationRequestPersistenceConverter.toPersistenceObject(couponCreationRequestDO);

        CouponBO couponBO = CouponBO.builder()
            .couponCode(couponCreationRequestPO.getCouponCode())
            .claimLimitCount(couponCreationRequestPO.getClaimLimitCount())
            .claimCount(couponCreationRequestPO.getClaimCount())
            .createdAt(couponCreationRequestPO.getCreatedAt())
            .build();

        couponRepository.save(couponBO);

        return CouponCreationResponseDO.builder()
            .message("coupon created OK")
            .build();
    }
}
