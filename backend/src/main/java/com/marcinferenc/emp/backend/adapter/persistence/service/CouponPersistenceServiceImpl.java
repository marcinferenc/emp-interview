package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.adapter.persistence.converter.CouponCreationRequestPersistenceConverter;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationRequestPO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponPersistenceServiceImpl implements CouponPersistenceService {
    private final CouponCreationRequestPersistenceConverter couponCreationRequestPersistenceConverter;
    private final CouponCreationRequestPersistenceConverter couponCreationResponsePersistenceConverter;

    @Override
    public CouponCreationResponseDO create(CouponCreationRequestDO couponCreationRequestDO) {
        CouponCreationRequestPO couponCreationRequestPO = couponCreationRequestPersistenceConverter.toPersistenceObject(couponCreationRequestDO);

        return CouponCreationResponseDO.builder()
            .message("coupon created OK")
            .build();
    }
}
