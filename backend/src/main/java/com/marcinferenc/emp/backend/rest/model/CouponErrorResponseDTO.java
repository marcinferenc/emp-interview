package com.marcinferenc.emp.backend.rest.model;

import com.marcinferenc.emp.backend.rest.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@Schema(description = "Error response returned by the coupon API.")
public class CouponErrorResponseDTO {
    @Schema(description = "Machine-readable error code.", example = "VALIDATION_ERROR")
    ErrorCode errorCode;

    @Schema(description = "Human-readable error message.", example = "Invalid request")
    String message;
}
