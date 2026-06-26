package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimResponseDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponApiServiceImpl implements CouponApiService {
    private final CouponValidationService couponValidationService;

    @Override
    public CouponClaimResponseDTO claim(CouponClaimRequestDTO couponClaimRequest) {
        couponValidationService.validate(couponClaimRequest);

        return CouponClaimResponseDTO.builder()
            .message("coupon claimed OK")
            .build();
    }

    @Override
    public CouponCreationResponseDTO create(CouponCreationRequestDTO couponCreationRequest) {
        couponValidationService.validate(couponCreationRequest);

        return CouponCreationResponseDTO.builder()
            .message("coupon created OK")
            .build();
    }
}
