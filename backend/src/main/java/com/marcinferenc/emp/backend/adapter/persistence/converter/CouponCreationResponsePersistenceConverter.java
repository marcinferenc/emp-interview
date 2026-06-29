package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationResponsePO;
import com.marcinferenc.emp.backend.adapter.persistence.model.CouponResponseStatusPO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponResponseStatusDO;
import com.marcinferenc.emp.backend.rest.model.CouponResponseStatusDTO;
import org.springframework.stereotype.Component;

@Component
public class CouponCreationResponsePersistenceConverter {

    public CouponCreationResponseDO toDomainObject(CouponCreationResponsePO persistenceObject) {
        if (persistenceObject == null) {
            return null;
        }

        return CouponCreationResponseDO.builder()
            .status(toDomainStatus(persistenceObject.getStatus()))
            .message(persistenceObject.getMessage())
            .build();
    }

    public CouponCreationResponsePO toPersistenceObject(CouponCreationResponseDO domainObject) {
        if (domainObject == null) {
            return null;
        }

        return CouponCreationResponsePO.builder()
            .message(domainObject.getMessage())
            .build();
    }

    private CouponResponseStatusDO toDomainStatus(CouponResponseStatusPO statusPO) {
        if (statusPO == null) {
            return null;
        }

        return CouponResponseStatusDO.valueOf(statusPO.name());
    }

}
