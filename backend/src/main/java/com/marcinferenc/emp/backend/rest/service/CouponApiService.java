package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimResponseDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationResponseDTO;

public interface CouponApiService {
    CouponClaimResponseDTO claim(CouponClaimRequestDTO couponClaimRequest);

    CouponCreationResponseDTO create(CouponCreationRequestDTO couponCreationRequest);
}
