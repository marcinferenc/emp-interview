package com.marcinferenc.emp.backend.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Coupon claim response status.")
public enum CouponResponseStatusDTO {
    SUCCESS,
    FAILURE
}
