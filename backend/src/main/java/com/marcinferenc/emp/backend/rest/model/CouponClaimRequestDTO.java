package com.marcinferenc.emp.backend.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Request body for claiming a coupon.")
public class CouponClaimRequestDTO {
    @Schema(description = "Coupon code to claim.", example = "SUMMER2026", requiredMode = Schema.RequiredMode.REQUIRED)
    String couponCode;

    @Schema(description = "Email address of the user claiming the coupon.", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    String userEmailId;
}
