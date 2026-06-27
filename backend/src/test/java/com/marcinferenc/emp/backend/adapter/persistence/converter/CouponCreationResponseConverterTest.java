package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationResponsePO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CouponCreationResponseConverterTest {
    private final CouponCreationResponseConverter converter = new CouponCreationResponseConverter();

    @Test
    void shouldConvertPersistenceObjectToDomainObject() {
        CouponCreationResponsePO persistenceObject = CouponCreationResponsePO.builder()
            .message("Coupon created")
            .build();

        CouponCreationResponseDO domainObject = converter.toDomainObject(persistenceObject);

        assertThat(domainObject.getMessage()).isEqualTo("Coupon created");
    }

    @Test
    void shouldConvertDomainObjectToPersistenceObject() {
        CouponCreationResponseDO domainObject = CouponCreationResponseDO.builder()
            .message("Coupon created")
            .build();

        CouponCreationResponsePO persistenceObject = converter.toPersistenceObject(domainObject);

        assertThat(persistenceObject.getMessage()).isEqualTo("Coupon created");
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertThat(converter.toDomainObject(null)).isNull();
        assertThat(converter.toPersistenceObject(null)).isNull();
    }
}
