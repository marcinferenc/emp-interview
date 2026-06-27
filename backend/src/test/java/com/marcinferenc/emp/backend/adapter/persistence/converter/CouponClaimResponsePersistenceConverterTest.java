package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponClaimResponsePO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CouponClaimResponsePersistenceConverterTest {
    private final CouponClaimResponsePersistenceConverter converter = new CouponClaimResponsePersistenceConverter();

    @Test
    void shouldConvertPersistenceObjectToDomainObject() {
        CouponClaimResponsePO persistenceObject = CouponClaimResponsePO.builder()
            .message("Coupon claimed")
            .build();

        CouponClaimResponseDO domainObject = converter.toDomainObject(persistenceObject);

        assertThat(domainObject.getMessage()).isEqualTo("Coupon claimed");
    }

    @Test
    void shouldConvertDomainObjectToPersistenceObject() {
        CouponClaimResponseDO domainObject = CouponClaimResponseDO.builder()
            .message("Coupon claimed")
            .build();

        CouponClaimResponsePO persistenceObject = converter.toPersistenceObject(domainObject);

        assertThat(persistenceObject.getMessage()).isEqualTo("Coupon claimed");
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertThat(converter.toDomainObject(null)).isNull();
        assertThat(converter.toPersistenceObject(null)).isNull();
    }
}
