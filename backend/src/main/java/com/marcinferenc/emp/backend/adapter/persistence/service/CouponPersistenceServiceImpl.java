package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.adapter.persistence.converter.CouponCreationRequestPersistenceConverter;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationRequestPO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponPersistenceServiceImpl implements CouponPersistenceService {
    private final CouponCreationRequestPersistenceConverter couponCreationRequestPersistenceConverter;

    @Override
    public void create(CouponCreationRequestDO couponCreationRequestDO) {
        CouponCreationRequestPO persistenceObject = couponCreationRequestPersistenceConverter.toPersistenceObject(couponCreationRequestDO);
    }
}
