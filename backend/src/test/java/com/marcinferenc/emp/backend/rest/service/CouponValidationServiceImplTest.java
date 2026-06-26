package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.rest.ErrorCode;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponValidationServiceImplTest {
    private final CouponValidationServiceImpl service = new CouponValidationServiceImpl();

    @Test
    void shouldPassValidationForValidRequest() {
        CouponCreationRequestDTO request = createValidRequest();

        assertThatCode(() -> service.validate(request)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidationForLowerCaseIsoCountryCode() {
        CouponCreationRequestDTO request = createValidRequest();
        request.setCountryCode("pl");

        assertThatCode(() -> service.validate(request)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowValidationErrorForNullRequest() {
        assertValidationError(null);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "-", "_"})
    void shouldThrowValidationErrorWhenCouponCodeDoesNotContainLetterOrDigit(String couponCode) {
        CouponCreationRequestDTO request = createValidRequest();
        request.setCouponCode(couponCode);

        assertValidationError(request);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"XX", "POL", "1", "P1"})
    void shouldThrowValidationErrorWhenCountryCodeIsNotIsoCountryCode(String countryCode) {
        CouponCreationRequestDTO request = createValidRequest();
        request.setCountryCode(countryCode);

        assertValidationError(request);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, Integer.MAX_VALUE})
    void shouldPassValidationForClaimLimitCountBoundaries(Integer claimLimitCount) {
        CouponCreationRequestDTO request = createValidRequest();
        request.setClaimLimitCount(claimLimitCount);

        assertThatCode(() -> service.validate(request)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowValidationErrorWhenClaimLimitCountIsNull() {
        CouponCreationRequestDTO request = createValidRequest();
        request.setClaimLimitCount(null);

        assertValidationError(request);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void shouldThrowValidationErrorWhenClaimLimitCountIsLessThanOne(Integer claimLimitCount) {
        CouponCreationRequestDTO request = createValidRequest();
        request.setClaimLimitCount(claimLimitCount);

        assertValidationError(request);
    }

    private void assertValidationError(CouponCreationRequestDTO request) {
        assertThatThrownBy(() -> service.validate(request))
            .isInstanceOfSatisfying(CouponException.class, exception ->
                assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.VALIDATION_ERROR));
    }

    private CouponCreationRequestDTO createValidRequest() {
        CouponCreationRequestDTO request = new TestCouponCreationRequestDTO();
        request.setCouponCode("SUMMER2026");
        request.setCountryCode("PL");
        request.setClaimLimitCount(10);
        return request;
    }

    private static class TestCouponCreationRequestDTO extends CouponCreationRequestDTO {
    }
}
