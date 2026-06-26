package com.marcinferenc.emp.backend.rest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class EmailValidationServiceImplTest {
    private final EmailValidationServiceImpl service = new EmailValidationServiceImpl();

    @ParameterizedTest
    @ValueSource(strings = {
        "user@example.com",
        "USER@example.COM",
        "first.last@example.co.uk",
        "user+tag@example-domain.com",
        "customer/department=shipping@example.com",
        "!#$%&'*+-/=?^_`{|}~@example.com",
        "a@b.co"
    })
    void shouldReturnTrueForValidEmailAddress(String emailAddress) {
        assertThat(service.isValidEmailAddress(emailAddress)).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
        " ",
        "plain-address",
        "@example.com",
        "user@",
        "user@@example.com",
        "user example@example.com",
        "user@example",
        "user@example.c",
        "user@example.123",
        ".user@example.com",
        "user.@example.com",
        "user..name@example.com",
        "user@example..com",
        "user@.example.com",
        "user@example.com.",
        "user@-example.com",
        "user@example-.com",
        "user@exa_mple.com",
        "user@[127.0.0.1]",
        "üser@example.com",
        "user@exämple.com"
    })
    void shouldReturnFalseForInvalidEmailAddress(String emailAddress) {
        assertThat(service.isValidEmailAddress(emailAddress)).isFalse();
    }

    @Test
    void shouldReturnFalseForNullEmailAddress() {
        assertThat(service.isValidEmailAddress(null)).isFalse();
    }

    @Test
    void shouldReturnTrueForMaximumAllowedEmailLength() {
        String emailAddress = "a".repeat(64) + "@" + "b".repeat(63) + "." + "c".repeat(63)
            + "." + "d".repeat(61);

        assertThat(emailAddress).hasSize(254);
        assertThat(service.isValidEmailAddress(emailAddress)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenEmailAddressIsLongerThanMaximumAllowedLength() {
        String emailAddress = "a".repeat(64) + "@" + "b".repeat(63) + "." + "c".repeat(63)
            + "." + "d".repeat(62);

        assertThat(emailAddress).hasSize(255);
        assertThat(service.isValidEmailAddress(emailAddress)).isFalse();
    }

    @Test
    void shouldReturnFalseWhenLocalPartIsLongerThanMaximumAllowedLength() {
        String emailAddress = "a".repeat(65) + "@example.com";

        assertThat(service.isValidEmailAddress(emailAddress)).isFalse();
    }

    @Test
    void shouldReturnFalseWhenDomainLabelIsLongerThanMaximumAllowedLength() {
        String emailAddress = "user@" + "a".repeat(64) + ".com";

        assertThat(service.isValidEmailAddress(emailAddress)).isFalse();
    }
}
