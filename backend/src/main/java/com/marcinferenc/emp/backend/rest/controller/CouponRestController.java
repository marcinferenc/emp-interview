package com.marcinferenc.emp.backend.rest.controller;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.service.CouponDomainService;
import com.marcinferenc.emp.backend.rest.converter.CouponClaimRequestConverter;
import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimResponseDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationResponseDTO;
import com.marcinferenc.emp.backend.rest.service.CouponApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CouponRestController {
    private final CouponApiService couponApiService;
    private final CouponDomainService couponDomainService;
    private final CouponClaimRequestConverter couponClaimRequestConverter;

    @PostMapping(path = "/claim", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CouponClaimResponseDTO claimCoupon(@RequestBody CouponClaimRequestDTO couponClaimRequest) {
        logRequest(couponClaimRequest);

        CouponClaimRequestDO couponClaimRequestDO = couponClaimRequestConverter.toDomainObject(couponClaimRequest);
        CouponClaimResponseDO couponClaimResponseDO = couponDomainService.claim(couponClaimRequestDO);

        return couponApiService.claim(couponClaimRequest);
    }

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CouponCreationResponseDTO createCoupon(@RequestBody CouponCreationRequestDTO couponCreationRequest) {
        logRequest(couponCreationRequest);
        return couponApiService.create(couponCreationRequest);
    }

    //---------------------------------------------------------
    private void logRequest(Object couponCreationRequest) {
        log.info("received request: {}", couponCreationRequest);
    }
}
