package com.marcinferenc.emp.backend.domain.service;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CouponDomainServiceImpl implements CouponDomainService {
    @Override
    public CouponClaimResponseDO claim(CouponClaimRequestDO couponClaimRequestDO) {
        return null;
    }
}
