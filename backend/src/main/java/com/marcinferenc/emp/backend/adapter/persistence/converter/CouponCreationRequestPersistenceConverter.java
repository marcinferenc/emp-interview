package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationRequestPO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import org.springframework.stereotype.Component;

public class CouponCreationRequestPersistenceConverter {

    public CouponCreationRequestDO toDomainObject(CouponCreationRequestPO persistenceObject) {
        if (persistenceObject == null) {
            return null;
        }

        return CouponCreationRequestDO.builder()
            .couponCode(persistenceObject.getCouponCode())
            .countryCode(persistenceObject.getCountryCode())
            .claimLimitCount(persistenceObject.getClaimLimitCount())
            .build();
    }

    public CouponCreationRequestPO toPersistenceObject(CouponCreationRequestDO domainObject) {
        if (domainObject == null) {
            return null;
        }

        return CouponCreationRequestPO.builder()
            .couponCode(domainObject.getCouponCode())
            .countryCode(domainObject.getCountryCode())
            .claimLimitCount(domainObject.getClaimLimitCount())
            .build();
    }
}
