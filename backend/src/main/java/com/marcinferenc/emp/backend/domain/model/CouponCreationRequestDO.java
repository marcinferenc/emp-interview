package com.marcinferenc.emp.backend.domain.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CouponCreationRequestDO {
    String couponCode;
    String countryCode;
    Integer claimLimitCount;
}
