package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CouponCreationRequestRestConverter {

    public CouponCreationRequestDO toDomainObject(CouponCreationRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        CouponCreationRequestDO domainObject = CouponCreationRequestDO.builder()
            .couponCode(toLowerCase(dto.getCouponCode()))
            .countryCode(StringUtils.upperCase(dto.getCountryCode()))
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

    private String toLowerCase(String value) {
        return value == null ? null : value.toLowerCase(Locale.ROOT);
    }
}
