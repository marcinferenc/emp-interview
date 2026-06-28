package com.marcinferenc.emp.backend.adapter.persistence.model;

import java.time.Instant;

public class CouponBO {
    Long id;
    String couponCode;
    String countryCode;
    Integer claimLimitCount;
    Integer claimCount;
    Instant createdAt;
}
