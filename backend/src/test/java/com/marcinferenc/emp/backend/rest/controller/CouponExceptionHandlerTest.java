package com.marcinferenc.emp.backend.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class CouponExceptionHandlerTest {
    @Test
    void shouldReturnNotFoundForMissingStaticResource() {
        CouponExceptionHandler exceptionHandler = new CouponExceptionHandler();

        ResponseEntity<Void> response = exceptionHandler.handleNoResourceFoundException();

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).isNull();
    }
}
