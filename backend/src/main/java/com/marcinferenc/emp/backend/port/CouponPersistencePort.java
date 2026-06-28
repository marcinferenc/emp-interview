package com.marcinferenc.emp.backend.port;

import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;

public interface CouponPersistencePort {
    CouponCreationResponseDO create(CouponCreationRequestDO couponCreationRequestDO);
}
