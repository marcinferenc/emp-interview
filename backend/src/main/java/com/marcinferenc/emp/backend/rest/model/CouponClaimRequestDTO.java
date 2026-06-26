package com.marcinferenc.emp.backend.rest.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CouponClaimRequestDTO {
    String couponCode;
    String userEmail;
}
