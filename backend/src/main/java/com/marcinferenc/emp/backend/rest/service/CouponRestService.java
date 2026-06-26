package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimResponseDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CouponRestService {

    @PostMapping(path = "/claim", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CouponClaimResponseDTO claimCoupon(CouponClaimRequestDTO couponClaimRequest) {
        logReceivedRequest(couponClaimRequest);
        return CouponClaimResponseDTO.builder()
            .message("coupon claimed OK")
            .build();
    }

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CouponCreationResponseDTO createCoupon(CouponCreationRequestDTO couponCreationRequest) {
        logReceivedRequest(couponCreationRequest);
        return CouponCreationResponseDTO.builder()
            .message("coupon created OK")
            .build();
    }

    //---------------------------------------------------------
    private void logReceivedRequest(Object couponCreationRequest) {
        log.info("received: {}", couponCreationRequest);
    }
}
