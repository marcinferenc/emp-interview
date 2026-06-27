package com.marcinferenc.emp.backend.adapter.persistence.converter;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponCreationResponsePO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import org.springframework.stereotype.Component;

@Component("persistenceCouponCreationResponseConverter")
public class CouponCreationResponseConverter {

    public CouponCreationResponseDO toDomainObject(CouponCreationResponsePO persistenceObject) {
        if (persistenceObject == null) {
            return null;
        }

        return CouponCreationResponseDO.builder()
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
}
