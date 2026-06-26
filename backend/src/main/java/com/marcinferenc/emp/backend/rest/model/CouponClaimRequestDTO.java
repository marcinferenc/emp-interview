package com.marcinferenc.emp.backend.rest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CouponClaimRequestDTO {
    String couponCode;
    String userEmailId;
}
