package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CouponClaimRequestRestConverter {

    public CouponClaimRequestDO toDomainObject(CouponClaimRequestDTO dto) {
        return toDomainObject(dto, null);
    }

    public CouponClaimRequestDO toDomainObject(CouponClaimRequestDTO dto, String ipAddress) {
        if (dto == null) {
            return null;
        }

        return CouponClaimRequestDO.builder()
            .couponCode(StringUtils.lowerCase(dto.getCouponCode()))
            .ipAddress(ipAddress)
            .userEmailId(dto.getUserEmailId())
            .build();
    }

    public CouponClaimRequestDTO toDto(CouponClaimRequestDO domainObject) {
        if (domainObject == null) {
            return null;
        }

        CouponClaimRequestDTO res = new CouponClaimRequestDTO();
        res.setCouponCode(domainObject.getCouponCode());
        res.setUserEmailId(domainObject.getUserEmailId());
        return res;
    }
}
