package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponClaimResponsePO;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponResponseStatusPO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponResponseStatusDO;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CouponClaimResponsePersistenceConverterTest {
    private final CouponClaimResponsePersistenceConverter converter = new CouponClaimResponsePersistenceConverter();

    @Test
    void shouldConvertPersistenceObjectToDomainObject() {
        Instant timestamp = Instant.parse("2026-06-29T12:00:00Z");
        CouponClaimResponsePO persistenceObject = CouponClaimResponsePO.builder()
            .status(CouponResponseStatusPO.SUCCESS)
            .couponCode("SUMMER-2026")
            .userEmailId("user@example.com")
            .timestamp(timestamp)
            .message("Coupon claimed")
            .build();

        CouponClaimResponseDO domainObject = converter.toDomainObject(persistenceObject);

        assertThat(domainObject.getStatus()).isEqualTo(CouponResponseStatusDO.SUCCESS);
        assertThat(domainObject.getCouponCode()).isEqualTo("SUMMER-2026");
        assertThat(domainObject.getUserEmailId()).isEqualTo("user@example.com");
        assertThat(domainObject.getTimestamp()).isEqualTo(timestamp);
        assertThat(domainObject.getMessage()).isEqualTo("Coupon claimed");
    }
}
