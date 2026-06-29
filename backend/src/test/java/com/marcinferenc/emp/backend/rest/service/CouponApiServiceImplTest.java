package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponResponseStatusDO;
import com.marcinferenc.emp.backend.domain.service.CouponDomainService;
import com.marcinferenc.emp.backend.rest.converter.CouponClaimRequestRestConverter;
import com.marcinferenc.emp.backend.rest.converter.CouponClaimResponseRestConverter;
import com.marcinferenc.emp.backend.rest.converter.CouponCreationRequestRestConverter;
import com.marcinferenc.emp.backend.rest.converter.CouponCreationResponseRestConverter;
import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponApiServiceImplTest {
    @Mock private CouponValidationService couponValidationService;
    @Mock private CouponDomainService couponDomainService;
    @Mock private HttpServletRequest httpServletRequest;
    @Mock private CouponCreationRequestRestConverter couponCreationRequestRestConverter;
    @Mock private CouponCreationResponseRestConverter couponCreationResponseRestConverter;

    private final CouponClaimRequestRestConverter couponClaimRequestRestConverter = new CouponClaimRequestRestConverter();
    private final CouponClaimResponseRestConverter couponClaimResponseRestConverter = new CouponClaimResponseRestConverter();

    @Test
    void shouldPassClientIpAddressFromHttpRequestWhenClaimingCoupon() {
        CouponApiServiceImpl service = new CouponApiServiceImpl(
            couponValidationService,
            couponDomainService,
            httpServletRequest,
            couponClaimRequestRestConverter,
            couponClaimResponseRestConverter,
            couponCreationRequestRestConverter,
            couponCreationResponseRestConverter
        );
        CouponClaimRequestDTO request = new CouponClaimRequestDTO();
        request.setCouponCode("SUMMER2026");
        request.setUserEmailId("user@example.com");
        when(httpServletRequest.getRemoteAddr()).thenReturn("192.0.2.10");
        when(couponDomainService.claim(org.mockito.ArgumentMatchers.any())).thenReturn(CouponClaimResponseDO.builder()
            .status(CouponResponseStatusDO.SUCCESS)
            .couponCode("summer2026")
            .userEmailId("user@example.com")
            .timestamp(Instant.now())
            .build());

        service.claim(request);

        ArgumentCaptor<CouponClaimRequestDO> requestCaptor = ArgumentCaptor.forClass(CouponClaimRequestDO.class);
        verify(couponDomainService).claim(requestCaptor.capture());
        assertThat(requestCaptor.getValue().getIpAddress()).isEqualTo("192.0.2.10");
    }
}
