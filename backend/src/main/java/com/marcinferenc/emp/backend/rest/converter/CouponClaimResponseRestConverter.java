package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.rest.model.CouponResponseStatusDTO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CouponClaimResponseRestConverter {
    public CouponClaimResponseDTO toDto(CouponClaimResponseDO domainObject) {
        if (domainObject == null) {
            return null;
        }

        return CouponClaimResponseDTO.builder()
            .status(toDtoStatus(domainObject))
            .couponCode(domainObject.getCouponCode())
            .userEmailId(domainObject.getUserEmailId())
            .timestamp(domainObject.getTimestamp())
            .message(domainObject.getMessage())
            .build();
    }

    private CouponResponseStatusDTO toDtoStatus(CouponClaimResponseDO domainObject) {
        if (domainObject.getStatus() == null) {
            return null;
        }

        return CouponResponseStatusDTO.valueOf(domainObject.getStatus().name());
    }
}
