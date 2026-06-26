package com.marcinferenc.emp.backend.rest.model;

import com.marcinferenc.emp.backend.rest.ErrorCode;
import lombok.ToString;

@ToString
public class CouponException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public CouponException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
