package com.marcinferenc.emp.backend.adapter.peristence.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CouponClaimRequestPO {
    String couponCode;
    String userEmailId;
}
