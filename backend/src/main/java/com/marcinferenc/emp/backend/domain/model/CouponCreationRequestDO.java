package com.marcinferenc.emp.backend.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.Instant;

@Builder
@Data
public class CouponCreationRequestDO {
    String couponCode;
    String countryCode;
    Integer claimLimitCount;
    Integer claimCount;
    Instant createdAt;
}
