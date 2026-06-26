package com.marcinferenc.emp.backend.rest.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class CouponCreationRequestDTO {
    String code;
    Instant createdAt;
    Long usageLimitCount;
}
