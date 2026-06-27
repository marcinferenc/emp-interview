package com.marcinferenc.emp.backend.adapter.persistence.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class CouponCreationRequestPO {
    String couponCode;
    String countryCode;
    Integer claimLimitCount;
    Integer claimCount;
    Instant createdAt;
}
