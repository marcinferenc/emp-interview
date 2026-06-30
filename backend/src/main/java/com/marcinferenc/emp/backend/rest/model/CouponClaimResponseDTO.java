package com.marcinferenc.emp.backend.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
@Schema(description = "Response returned after claiming a coupon.")
public class CouponClaimResponseDTO {
    @Schema(description = "Claim result status.", example = "SUCCESS")
    CouponResponseStatusDTO status;

    @Schema(description = "Claimed coupon code.", example = "SUMMER2026")
    String couponCode;

    @Schema(description = "Email address of the user who claimed the coupon.", example = "user@example.com")
    String userEmailId;

    @Schema(description = "Claim processing timestamp.", example = "2026-06-30T12:34:56Z")
    Instant timestamp;

    @Schema(description = "Human-readable result message.", example = "Coupon claimed successfully")
    String message;
}
