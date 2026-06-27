package com.marcinferenc.emp.backend.adapter.persistence.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CouponClaimRequestPO {
    String couponCode;
    String userEmailId;
}
