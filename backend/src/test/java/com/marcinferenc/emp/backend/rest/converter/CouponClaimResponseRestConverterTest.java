package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponResponseStatusDO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimResponseDTO;
import com.marcinferenc.emp.backend.rest.model.CouponResponseStatusDTO;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CouponClaimResponseRestConverterTest {
    private final CouponClaimResponseRestConverter converter = new CouponClaimResponseRestConverter();

    @Test
    void shouldConvertDomainObjectToDto() {
        Instant timestamp = Instant.parse("2026-06-29T12:00:00Z");
        CouponClaimResponseDO domainObject = CouponClaimResponseDO.builder()
            .status(CouponResponseStatusDO.SUCCESS)
            .couponCode("SUMMER-2026")
            .userEmailId("user@example.com")
            .timestamp(timestamp)
            .message("coupon claimed OK")
            .build();

        CouponClaimResponseDTO dto = converter.toDto(domainObject);

        assertThat(dto.getStatus()).isEqualTo(CouponResponseStatusDTO.SUCCESS);
        assertThat(dto.getCouponCode()).isEqualTo("SUMMER-2026");
        assertThat(dto.getUserEmailId()).isEqualTo("user@example.com");
        assertThat(dto.getTimestamp()).isEqualTo(timestamp);
        assertThat(dto.getMessage()).isEqualTo("coupon claimed OK");
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertThat(converter.toDto(null)).isNull();
    }
}
