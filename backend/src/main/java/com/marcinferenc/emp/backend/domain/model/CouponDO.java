package com.marcinferenc.emp.backend.domain.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CouponDO {
    String couponCode;
    String countryCode;
}
