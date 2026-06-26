package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class CouponClaimRequestConverter {

    public CouponClaimRequestDO toDomainObject(CouponClaimRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return CouponClaimRequestDO.builder()
            .couponCode(dto.getCouponCode())
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
