package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimResponseDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CouponClaimResponseRestConverterTest {
    private final CouponClaimResponseRestConverter converter = new CouponClaimResponseRestConverter();

    @Test
    void shouldConvertDtoToDomainObject() {
        CouponClaimResponseDTO dto = CouponClaimResponseDTO.builder()
            .message("coupon claimed OK")
            .build();

        CouponClaimResponseDO domainObject = converter.toDomainObject(dto);

        assertThat(domainObject.getMessage()).isEqualTo("coupon claimed OK");
    }

    @Test
    void shouldConvertDomainObjectToDto() {
        CouponClaimResponseDO domainObject = CouponClaimResponseDO.builder()
            .message("coupon claimed OK")
            .build();

        CouponClaimResponseDTO dto = converter.toDto(domainObject);

        assertThat(dto.getMessage()).isEqualTo("coupon claimed OK");
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertThat(converter.toDomainObject(null)).isNull();
        assertThat(converter.toDto(null)).isNull();
    }
}
