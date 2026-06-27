package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationRequestPO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CouponCreationRequestConverterTest {
    private final CouponCreationRequestConverter converter = new CouponCreationRequestConverter();

    @Test
    void shouldConvertPersistenceObjectToDomainObject() {
        CouponCreationRequestPO persistenceObject = CouponCreationRequestPO.builder()
            .couponCode("summer2026")
            .countryCode("PL")
            .claimLimitCount(10)
            .build();

        CouponCreationRequestDO domainObject = converter.toDomainObject(persistenceObject);

        assertThat(domainObject.getCouponCode()).isEqualTo("summer2026");
        assertThat(domainObject.getCountryCode()).isEqualTo("PL");
        assertThat(domainObject.getClaimLimitCount()).isEqualTo(10);
    }

    @Test
    void shouldConvertDomainObjectToPersistenceObject() {
        CouponCreationRequestDO domainObject = CouponCreationRequestDO.builder()
            .couponCode("summer2026")
            .countryCode("PL")
            .claimLimitCount(10)
            .build();

        CouponCreationRequestPO persistenceObject = converter.toPersistenceObject(domainObject);

        assertThat(persistenceObject.getCouponCode()).isEqualTo("summer2026");
        assertThat(persistenceObject.getCountryCode()).isEqualTo("PL");
        assertThat(persistenceObject.getClaimLimitCount()).isEqualTo(10);
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertThat(converter.toDomainObject(null)).isNull();
        assertThat(converter.toPersistenceObject(null)).isNull();
    }
}
