package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CouponValidationServiceImpl implements CouponValidationService {

    @Override
    public void validate(CouponCreationRequestDTO creationRequest) {

    }

    @Override
    public void validate(CouponClaimRequestDTO claimRequest) {

    }
}
