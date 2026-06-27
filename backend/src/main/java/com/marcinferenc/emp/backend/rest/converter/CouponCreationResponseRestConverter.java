package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CouponCreationResponseRestConverter {

    public CouponCreationResponseDO toDomainObject(CouponCreationResponseDTO dto) {
        if (dto == null) {
            return null;
        }

        return CouponCreationResponseDO.builder()
            .message(dto.getMessage())
            .build();
    }

    public CouponCreationResponseDTO toDto(CouponCreationResponseDO domainObject) {
        if (domainObject == null) {
            return null;
        }

        return CouponCreationResponseDTO.builder()
            .message(domainObject.getMessage())
            .build();
    }
}
