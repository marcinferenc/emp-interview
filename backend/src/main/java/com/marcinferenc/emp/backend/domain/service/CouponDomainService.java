package com.marcinferenc.emp.backend.domain.service;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;

public interface CouponDomainService {
    CouponClaimResponseDO claim(CouponClaimRequestDO couponClaimRequestDO);
    CouponCreationResponseDO create(CouponCreationRequestDO couponCreationRequestDO);
}
