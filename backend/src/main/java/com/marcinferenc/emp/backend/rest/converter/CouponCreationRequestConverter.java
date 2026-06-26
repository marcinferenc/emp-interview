package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class CouponCreationRequestConverter {

    public CouponCreationRequestDO toDomainObject(CouponCreationRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        CouponCreationRequestDO domainObject = CouponCreationRequestDO.builder()
            .couponCode(dto.getCouponCode())
            .countryCode(dto.getCountryCode())
            .claimLimitCount(dto.getClaimLimitCount())
            .build();

        return domainObject;
    }

    public CouponCreationRequestDTO toDto(CouponCreationRequestDO domainObject) {
        if (domainObject == null) {
            return null;
        }

        CouponCreationRequestDTO dto = new CouponCreationRequestDTO();
        dto.setCouponCode(domainObject.getCouponCode());
        dto.setCountryCode(domainObject.getCountryCode());
        dto.setClaimLimitCount(domainObject.getClaimLimitCount());
        return dto;
    }
}
