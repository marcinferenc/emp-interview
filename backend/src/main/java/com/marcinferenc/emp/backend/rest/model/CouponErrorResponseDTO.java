package com.marcinferenc.emp.backend.rest.model;

import com.marcinferenc.emp.backend.rest.ErrorCode;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CouponErrorResponseDTO {
    ErrorCode errorCode;
    String message;
}
