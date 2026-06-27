package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponClaimRequestPO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import org.springframework.stereotype.Component;

@Component("persistenceCouponClaimRequestConverter")
public class CouponClaimRequestConverter {

    public CouponClaimRequestDO toDomainObject(CouponClaimRequestPO persistenceObject) {
        if (persistenceObject == null) {
            return null;
        }

        return CouponClaimRequestDO.builder()
            .couponCode(persistenceObject.getCouponCode())
            .userEmailId(persistenceObject.getUserEmailId())
            .build();
    }

    public CouponClaimRequestPO toPersistenceObject(CouponClaimRequestDO domainObject) {
        if (domainObject == null) {
            return null;
        }

        return CouponClaimRequestPO.builder()
            .couponCode(domainObject.getCouponCode())
            .userEmailId(domainObject.getUserEmailId())
            .build();
    }
}
