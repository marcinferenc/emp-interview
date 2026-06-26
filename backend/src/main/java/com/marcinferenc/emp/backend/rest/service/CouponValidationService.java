package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;

public interface CouponValidationService {
    void validate(CouponCreationRequestDTO creationRequest);
    void validate(CouponClaimRequestDTO claimRequest);
}
