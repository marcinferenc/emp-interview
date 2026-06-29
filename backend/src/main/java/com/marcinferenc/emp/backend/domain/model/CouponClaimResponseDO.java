package com.marcinferenc.emp.backend.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class CouponClaimResponseDO {
    CouponResponseStatusDO status;
    String couponCode;
    String userEmailId;
    Instant timestamp;
    String message;
}
