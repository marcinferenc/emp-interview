package com.marcinferenc.emp.backend.port;

import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;

public interface CouponPersistencePort {
    void create(CouponCreationRequestDO couponCreationRequestDO);
}
