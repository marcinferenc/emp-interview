package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponClaimRequestPO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CouponClaimRequestPersistenceConverterTest {
    private final CouponClaimRequestPersistenceConverter converter = new CouponClaimRequestPersistenceConverter();

    @Test
    void shouldConvertPersistenceObjectToDomainObject() {
        CouponClaimRequestPO persistenceObject = CouponClaimRequestPO.builder()
            .couponCode("summer2026")
            .userEmailId("user@example.com")
            .build();

        CouponClaimRequestDO domainObject = converter.toDomainObject(persistenceObject);

        assertThat(domainObject.getCouponCode()).isEqualTo("summer2026");
        assertThat(domainObject.getUserEmailId()).isEqualTo("user@example.com");
    }

    @Test
    void shouldConvertDomainObjectToPersistenceObject() {
        CouponClaimRequestDO domainObject = CouponClaimRequestDO.builder()
            .couponCode("summer2026")
            .userEmailId("user@example.com")
            .build();

        CouponClaimRequestPO persistenceObject = converter.toPersistenceObject(domainObject);

        assertThat(persistenceObject.getCouponCode()).isEqualTo("summer2026");
        assertThat(persistenceObject.getUserEmailId()).isEqualTo("user@example.com");
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertThat(converter.toDomainObject(null)).isNull();
        assertThat(converter.toPersistenceObject(null)).isNull();
    }
}
