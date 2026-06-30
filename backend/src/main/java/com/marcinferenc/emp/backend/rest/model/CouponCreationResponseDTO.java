package com.marcinferenc.emp.backend.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@Schema(description = "Response returned after creating a coupon.")
public class CouponCreationResponseDTO {
    @Schema(description = "Human-readable result message.", example = "Coupon created successfully")
    String message;
}
