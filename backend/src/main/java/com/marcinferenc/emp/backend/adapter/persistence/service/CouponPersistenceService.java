package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;

public interface CouponPersistenceService {
    void create(CouponCreationRequestDO couponCreationRequestDO);
}
