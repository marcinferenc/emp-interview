package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationRequestPO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CouponCreationRequestPersistenceConverterTest {
    private final CouponCreationRequestPersistenceConverter converter = new CouponCreationRequestPersistenceConverter();

    @Test
    void shouldConvertPersistenceObjectToDomainObject() {
        Instant createdAt = Instant.parse("2026-06-27T10:15:30Z");
        CouponCreationRequestPO persistenceObject = CouponCreationRequestPO.builder()
            .couponCode("summer2026")
            .countryCode("PL")
            .claimLimitCount(10)
            .claimCount(3)
            .createdAt(createdAt)
            .build();

        CouponCreationRequestDO domainObject = converter.toDomainObject(persistenceObject);

        assertThat(domainObject.getCouponCode()).isEqualTo("summer2026");
        assertThat(domainObject.getCountryCode()).isEqualTo("PL");
        assertThat(domainObject.getClaimLimitCount()).isEqualTo(10);
        assertThat(domainObject.getClaimCount()).isEqualTo(3);
        assertThat(domainObject.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldConvertDomainObjectToPersistenceObject() {
        Instant createdAt = Instant.parse("2026-06-27T10:15:30Z");
        CouponCreationRequestDO domainObject = CouponCreationRequestDO.builder()
            .couponCode("summer2026")
            .countryCode("PL")
            .claimLimitCount(10)
            .claimCount(3)
            .createdAt(createdAt)
            .build();

        CouponCreationRequestPO persistenceObject = converter.toPersistenceObject(domainObject);

        assertThat(persistenceObject.getCouponCode()).isEqualTo("summer2026");
        assertThat(persistenceObject.getCountryCode()).isEqualTo("PL");
        assertThat(persistenceObject.getClaimLimitCount()).isEqualTo(10);
        assertThat(persistenceObject.getClaimCount()).isEqualTo(3); //business logic forbids that
        assertThat(persistenceObject.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertThat(converter.toDomainObject(null)).isNull();
        assertThat(converter.toPersistenceObject(null)).isNull();
    }
}
