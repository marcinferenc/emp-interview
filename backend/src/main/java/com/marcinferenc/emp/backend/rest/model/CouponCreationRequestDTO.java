package com.marcinferenc.emp.backend.rest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CouponCreationRequestDTO {
    String couponCode;
    String countryCode;
    Integer claimLimitCount;
}
