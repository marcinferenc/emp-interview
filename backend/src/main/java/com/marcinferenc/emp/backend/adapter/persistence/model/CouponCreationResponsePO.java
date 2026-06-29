package com.marcinferenc.emp.backend.adapter.persistence.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CouponCreationResponsePO {
    CouponResponseStatusPO status;
    String message;
}
