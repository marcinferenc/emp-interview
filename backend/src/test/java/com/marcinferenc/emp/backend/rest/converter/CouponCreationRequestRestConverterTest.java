package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CouponCreationRequestRestConverterTest {
    private final CouponCreationRequestRestConverter converter = new CouponCreationRequestRestConverter();

    @Test
    void shouldConvertDtoToDomainObject() {
        CouponCreationRequestDTO dto = new CouponCreationRequestDTO();
        dto.setCouponCode("SUMMER2026");
        dto.setCountryCode("PL");
        dto.setClaimLimitCount(10);

        CouponCreationRequestDO domainObject = converter.toDomainObject(dto);

        assertThat(domainObject.getCouponCode()).isEqualTo("summer2026");
        assertThat(domainObject.getCountryCode()).isEqualTo("PL");
        assertThat(domainObject.getClaimLimitCount()).isEqualTo(10);
    }

    @Test
    void shouldConvertCouponCodeToLowerCaseWhenConvertingDtoToDomainObject() {
        CouponCreationRequestDTO dto = new CouponCreationRequestDTO();
        dto.setCouponCode("SuMmEr2026");

        CouponCreationRequestDO domainObject = converter.toDomainObject(dto);

        assertThat(domainObject.getCouponCode()).isEqualTo("summer2026");
    }

    @Test
    void shouldConvertDomainObjectToDto() {
        CouponCreationRequestDO domainObject = CouponCreationRequestDO.builder()
            .couponCode("SUMMER2026")
            .countryCode("PL")
            .claimLimitCount(10)
            .build();

        CouponCreationRequestDTO dto = converter.toDto(domainObject);

        assertThat(dto.getCouponCode()).isEqualTo("SUMMER2026");
        assertThat(dto.getCountryCode()).isEqualTo("PL");
        assertThat(dto.getClaimLimitCount()).isEqualTo(10);
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertThat(converter.toDomainObject(null)).isNull();
        assertThat(converter.toDto(null)).isNull();
    }
}
