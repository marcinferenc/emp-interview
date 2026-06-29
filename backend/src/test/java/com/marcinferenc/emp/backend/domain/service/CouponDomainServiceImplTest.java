package com.marcinferenc.emp.backend.domain.service;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponDO;
import com.marcinferenc.emp.backend.port.CouponPersistencePort;
import com.marcinferenc.emp.backend.port.IpInfoPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponDomainServiceImplTest {
    @InjectMocks private CouponDomainServiceImpl service;
    @Mock private CouponPersistencePort couponPersistencePort;
    @Mock private IpInfoPort ipInfoPort;

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
    void shouldInitializeClaimCountAndCreatedAtWhenCreatingCoupon() {
        CouponCreationRequestDO request = CouponCreationRequestDO.builder()
            .couponCode("summer2026")
            .countryCode("PL")
            .claimLimitCount(10)
            .build();
        Instant beforeCreate = Instant.now();

        service.create(request);

        Instant afterCreate = Instant.now();
        assertThat(request.getClaimCount()).isZero();
        assertThat(request.getCreatedAt()).isBetween(beforeCreate, afterCreate);
    }

    @Test
    void shouldThrowExceptionWhenClaimCountIsAlreadyPopulatedBeforeCreatingCoupon() {
        CouponCreationRequestDO request = CouponCreationRequestDO.builder()
            .couponCode("summer2026")
            .countryCode("PL")
            .claimLimitCount(10)
            .claimCount(1)
            .build();

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenCreatedAtIsAlreadyPopulatedBeforeCreatingCoupon() {
        CouponCreationRequestDO request = CouponCreationRequestDO.builder()
            .couponCode("summer2026")
            .countryCode("PL")
            .claimLimitCount(10)
            .createdAt(Instant.now())
            .build();

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldClaimCouponWhenCouponCodeHasNoUpperCaseLetters() {
        CouponClaimRequestDO request = CouponClaimRequestDO.builder()
            .couponCode("summer2026")
            .ipAddress("127.0.0.1")
            .userEmailId("user@example.com")
            .build();
        when(ipInfoPort.getCountryCode("127.0.0.1")).thenReturn("PL");
        when(couponPersistencePort.find("summer2026", "PL")).thenReturn(Optional.of(CouponDO.builder()
            .couponCode("summer2026")
            .countryCode("PL")
            .build()));
        when(couponPersistencePort.claim(request)).thenReturn(CouponClaimResponseDO.builder()
            .couponCode("summer2026")
            .userEmailId("user@example.com")
            .build());

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
