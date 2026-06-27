package com.marcinferenc.emp.backend.port;

import com.marcinferenc.emp.backend.adapter.persistence.service.CouponPersistenceService;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponPersistenceAdapterPort implements CouponPersistencePort {
    private final CouponPersistenceService couponPersistenceService;

    @Override
    public void create(CouponCreationRequestDO couponCreationRequestDO) {
        couponPersistenceService.create(couponCreationRequestDO);
    }
}
