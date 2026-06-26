package com.marcinferenc.emp.backend.rest.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCreationRequestDTO {
    String couponCode;
    String countryCode;
    Integer claimLimitCount;
}
