package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationResponseDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CouponCreationResponseRestConverterTest {
    private final CouponCreationResponseRestConverter converter = new CouponCreationResponseRestConverter();

    @Test
    void shouldConvertDtoToDomainObject() {
        CouponCreationResponseDTO dto = CouponCreationResponseDTO.builder()
            .message("coupon created OK")
            .build();

        CouponCreationResponseDO domainObject = converter.toDomainObject(dto);

        assertThat(domainObject.getMessage()).isEqualTo("coupon created OK");
    }

    @Test
    void shouldConvertDomainObjectToDto() {
        CouponCreationResponseDO domainObject = CouponCreationResponseDO.builder()
            .message("coupon created OK")
            .build();

        CouponCreationResponseDTO dto = converter.toDto(domainObject);

        assertThat(dto.getMessage()).isEqualTo("coupon created OK");
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertThat(converter.toDomainObject(null)).isNull();
        assertThat(converter.toDto(null)).isNull();
    }
}
