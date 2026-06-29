package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponClaimResponsePO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponResponseStatusDO;
import org.springframework.stereotype.Component;

@Component
public class CouponClaimResponsePersistenceConverter {

    public CouponClaimResponseDO toDomainObject(CouponClaimResponsePO persistenceObject) {
        if (persistenceObject == null) {
            return null;
        }

        return CouponClaimResponseDO.builder()
            .status(toDomainStatus(persistenceObject))
            .couponCode(persistenceObject.getCouponCode())
            .userEmailId(persistenceObject.getUserEmailId())
            .timestamp(persistenceObject.getTimestamp())
            .message(persistenceObject.getMessage())
            .build();
    }

    private CouponResponseStatusDO toDomainStatus(CouponClaimResponsePO persistenceObject) {
        if (persistenceObject.getStatus() == null) {
            return null;
        }

        return CouponResponseStatusDO.valueOf(persistenceObject.getStatus().name());
    }
}
