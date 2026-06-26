package com.marcinferenc.emp.backend.domain.service;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;

public interface CouponDomainService {
    CouponClaimResponseDO claim(CouponClaimRequestDO couponClaimRequestDO);
}
