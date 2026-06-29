package com.marcinferenc.emp.backend.port;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponDO;

import java.util.Optional;

public interface CouponPersistencePort {
    CouponCreationResponseDO create(CouponCreationRequestDO couponCreationRequestDO);
    CouponClaimResponseDO claim(CouponClaimRequestDO couponClaimRequestDO);
    Optional<CouponDO> find(String couponCode, String countryCode);
}
