package com.marcinferenc.emp.backend.rest;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Coupon API error code.")
public enum ErrorCode {
    VALIDATION_ERROR,
    COUPON_NOT_FOUND,
    CLAIM_LIMIT_EXCEEDED,
    COUNTRY_CODE_UNKNOWN,
    COUNTRY_CODE_DETECTION_FAILED
}
