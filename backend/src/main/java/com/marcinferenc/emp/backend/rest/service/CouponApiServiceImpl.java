package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimResponseDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CouponApiServiceImpl implements CouponApiService {

    @Override
    public CouponClaimResponseDTO claim(CouponClaimRequestDTO couponClaimRequest) {
        return CouponClaimResponseDTO.builder()
            .message("coupon claimed OK")
            .build();
    }

    @Override
    public CouponCreationResponseDTO create(CouponCreationRequestDTO couponCreationRequest) {
        return CouponCreationResponseDTO.builder()
            .message("coupon created OK")
            .build();
    }
}
