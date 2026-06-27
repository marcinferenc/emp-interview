package com.marcinferenc.emp.backend.domain.service;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.port.CouponPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CouponDomainServiceImplTest {
    @InjectMocks private CouponDomainServiceImpl service;
    @Mock private CouponPersistencePort couponPersistencePort;

    @Test
    void shouldCreateCouponWhenCouponCodeHasNoUpperCaseLetters() {
        CouponCreationRequestDO request = CouponCreationRequestDO.builder()
            .couponCode("summer2026")
            .countryCode("PL")
            .claimLimitCount(10)
            .build();

        assertThatCode(() -> service.create(request)).doesNotThrowAnyException();
    }

    @Test
    void shouldClaimCouponWhenCouponCodeHasNoUpperCaseLetters() {
        CouponClaimRequestDO request = CouponClaimRequestDO.builder()
            .couponCode("summer2026")
            .userEmailId("user@example.com")
            .build();

        assertThatCode(() -> service.claim(request)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenCreationCouponCodeHasUpperCaseLetters() {
        CouponCreationRequestDO request = CouponCreationRequestDO.builder()
            .couponCode("Summer2026")
            .countryCode("PL")
            .claimLimitCount(10)
            .build();

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Coupon code must not contain upper case letters");
    }

    @Test
    void shouldThrowExceptionWhenClaimCouponCodeHasUpperCaseLetters() {
        CouponClaimRequestDO request = CouponClaimRequestDO.builder()
            .couponCode("Summer2026")
            .userEmailId("user@example.com")
            .build();

        assertThatThrownBy(() -> service.claim(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Coupon code must not contain upper case letters");
    }
}
