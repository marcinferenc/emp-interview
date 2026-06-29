package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CouponClaimResponseRestConverter {
    public CouponClaimResponseDTO toDto(CouponClaimResponseDO domainObject) {
        if (domainObject == null) {
            return null;
        }

        return CouponClaimResponseDTO.builder()
            .message(domainObject.getMessage())
            .build();
    }
}
