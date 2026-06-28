package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;

public interface CouponPersistenceService {
    CouponCreationResponseDO create(CouponCreationRequestDO couponCreationRequestDO);
}
