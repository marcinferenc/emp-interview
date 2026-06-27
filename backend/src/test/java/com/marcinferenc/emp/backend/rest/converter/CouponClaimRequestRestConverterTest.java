package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CouponClaimRequestRestConverterTest {
    private final CouponClaimRequestRestConverter converter = new CouponClaimRequestRestConverter();

    @Test
    void shouldConvertDtoToDomainObject() {
        CouponClaimRequestDTO dto = new CouponClaimRequestDTO();
        dto.setCouponCode("SUMMER2026");
        dto.setUserEmailId("user@example.com");

        CouponClaimRequestDO domainObject = converter.toDomainObject(dto);

        assertThat(domainObject.getCouponCode()).isEqualTo("summer2026");
        assertThat(domainObject.getUserEmailId()).isEqualTo("user@example.com");
    }

    @Test
    void shouldConvertCouponCodeToLowerCaseWhenConvertingDtoToDomainObject() {
        CouponClaimRequestDTO dto = new CouponClaimRequestDTO();
        dto.setCouponCode("SuMmEr2026");

        CouponClaimRequestDO domainObject = converter.toDomainObject(dto);

        assertThat(domainObject.getCouponCode()).isEqualTo("summer2026");
    }

    @Test
    void shouldConvertDomainObjectToDto() {
        CouponClaimRequestDO domainObject = CouponClaimRequestDO.builder()
            .couponCode("SUMMER2026")
            .userEmailId("user@example.com")
            .build();

        CouponClaimRequestDTO dto = converter.toDto(domainObject);

        assertThat(dto.getCouponCode()).isEqualTo("SUMMER2026");
        assertThat(dto.getUserEmailId()).isEqualTo("user@example.com");
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertThat(converter.toDomainObject(null)).isNull();
        assertThat(converter.toDto(null)).isNull();
    }
}
