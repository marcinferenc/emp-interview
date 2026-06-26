package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CouponClaimResponseConverter {

    public CouponClaimResponseDO toDomainObject(CouponClaimResponseDTO dto) {
        if (dto == null) {
            return null;
        }

        return CouponClaimResponseDO.builder()
            .message(dto.getMessage())
            .build();
    }

    public CouponClaimResponseDTO toDto(CouponClaimResponseDO domainObject) {
        if (domainObject == null) {
            return null;
        }

        return CouponClaimResponseDTO.builder()
            .message(domainObject.getMessage())
            .build();
    }
}
