package com.marcinferenc.emp.backend.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Request body for coupon creation.")
public class CouponCreationRequestDTO {
    @Schema(description = "Unique coupon code.", example = "SUMMER2026", requiredMode = Schema.RequiredMode.REQUIRED)
    String couponCode;

    @Schema(description = "ISO 3166-1 alpha-2 country code where the coupon can be claimed.", example = "PL", requiredMode = Schema.RequiredMode.REQUIRED)
    String countryCode;

    @Schema(description = "Maximum number of successful claims.", example = "100", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer claimLimitCount;
}
