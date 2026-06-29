package com.marcinferenc.emp.backend.rest.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class CouponClaimResponseDTO {
    CouponResponseStatusDTO status;
    String couponCode;
    String userEmailId;
    Instant timestamp;
    String message;
}
