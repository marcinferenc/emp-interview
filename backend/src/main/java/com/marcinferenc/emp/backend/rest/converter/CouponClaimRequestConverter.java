package com.marcinferenc.emp.backend.rest.converter;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CouponClaimRequestConverter {

    public CouponClaimRequestDO toDomainObject(CouponClaimRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return CouponClaimRequestDO.builder()
            .couponCode(toLowerCase(dto.getCouponCode()))
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

    private String toLowerCase(String value) {
        return value == null ? null : value.toLowerCase(Locale.ROOT);
    }
}
