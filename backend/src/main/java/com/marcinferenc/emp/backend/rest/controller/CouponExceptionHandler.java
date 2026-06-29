package com.marcinferenc.emp.backend.rest.controller;

import com.marcinferenc.emp.backend.rest.model.CouponErrorResponseDTO;
import com.marcinferenc.emp.backend.rest.model.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CouponExceptionHandler {

    @ExceptionHandler(CouponException.class)
    @SuppressWarnings("unused")
    public ResponseEntity<CouponErrorResponseDTO> handleCouponException(CouponException exception) {
        log.warn("Coupon exception occurred: {}", exception.toString());

        CouponErrorResponseDTO response = CouponErrorResponseDTO.builder()
            .errorCode(exception.getErrorCode())
            .message(exception.getMessage())
            .build();

        return ResponseEntity
            .status(resolveStatus(exception))
            .body(response);
    }

    @ExceptionHandler(Exception.class)
    @SuppressWarnings("unused")
    public ResponseEntity<CouponErrorResponseDTO> handleException(Exception exception) {
        log.error("Unexpected exception occurred", exception);

        CouponErrorResponseDTO response = CouponErrorResponseDTO.builder()
            .message("Unexpected error occurred")
            .build();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }

    private HttpStatus resolveStatus(CouponException exception) {
        return switch (exception.getErrorCode()) {
            case VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
            case COUPON_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CLAIM_LIMIT_EXCEEDED -> HttpStatus.CONFLICT;
            case COUNTRY_CODE_UNKNOWN -> HttpStatus.INTERNAL_SERVER_ERROR;
            case COUNTRY_CODE_DETECTION_FAILED -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
