package com.marcinferenc.emp.backend.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

@Builder
@Data
public class CouponClaimRequestDO {
    String couponCode;
    String countryCode;
    String ipAddress;
    String userEmailId;
}
