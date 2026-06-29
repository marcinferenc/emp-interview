package com.marcinferenc.emp.backend.adapter.persistence.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class CouponClaimResponsePO {
    CouponResponseStatusPO status;
    String couponCode;
    String userEmailId;
    Instant timestamp;
    String message;
}