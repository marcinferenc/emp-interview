package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.rest.ErrorCode;
import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
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
    private final CouponValidationServiceImpl service = new CouponValidationServiceImpl(new EmailValidationServiceImpl());

    @Test
    void shouldPassValidationForValidRequest() {
        CouponCreationRequestDTO request = createValidRequest();

        assertThatCode(() -> service.validate(request)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassClaimValidationForValidRequest() {
        CouponClaimRequestDTO request = createValidClaimRequest();

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
        assertValidationError((CouponCreationRequestDTO) null);
    }

    @Test
    void shouldThrowClaimValidationErrorForNullRequest() {
        assertValidationError((CouponClaimRequestDTO) null);
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
    @ValueSource(strings = {" ", "-", "_"})
    void shouldThrowClaimValidationErrorWhenCouponCodeDoesNotContainLetterOrDigit(String couponCode) {
        CouponClaimRequestDTO request = createValidClaimRequest();
        request.setCouponCode(couponCode);

        assertValidationError(request);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"plainaddress", "@example.com", "user@", "user@example", "user..name@example.com"})
    void shouldThrowClaimValidationErrorWhenUserEmailIdIsInvalid(String userEmailId) {
        CouponClaimRequestDTO request = createValidClaimRequest();
        request.setUserEmailId(userEmailId);

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

    private void assertValidationError(CouponClaimRequestDTO request) {
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

    private CouponClaimRequestDTO createValidClaimRequest() {
        CouponClaimRequestDTO request = new TestCouponClaimRequestDTO();
        request.setCouponCode("SUMMER2026");
        request.setUserEmailId("user@example.com");
        return request;
    }

    private static class TestCouponCreationRequestDTO extends CouponCreationRequestDTO {
    }

    private static class TestCouponClaimRequestDTO extends CouponClaimRequestDTO {
    }
}
