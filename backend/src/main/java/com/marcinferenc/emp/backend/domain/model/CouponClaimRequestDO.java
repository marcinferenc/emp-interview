package com.marcinferenc.emp.backend.domain.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CouponClaimRequestDO {
    String couponCode;
    String userEmailId;
}
