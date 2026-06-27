package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponClaimResponsePO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import org.springframework.stereotype.Component;

public class CouponClaimResponsePersistenceConverter {

    public CouponClaimResponseDO toDomainObject(CouponClaimResponsePO persistenceObject) {
        if (persistenceObject == null) {
            return null;
        }

        return CouponClaimResponseDO.builder()
            .message(persistenceObject.getMessage())
            .build();
    }

    public CouponClaimResponsePO toPersistenceObject(CouponClaimResponseDO domainObject) {
        if (domainObject == null) {
            return null;
        }

        return CouponClaimResponsePO.builder()
            .message(domainObject.getMessage())
            .build();
    }
}
