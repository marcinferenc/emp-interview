package com.marcinferenc.emp.backend.adapter.peristence.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CouponCreationRequestPO {
    String couponCode;
    String countryCode;
    Integer claimLimitCount;
}
