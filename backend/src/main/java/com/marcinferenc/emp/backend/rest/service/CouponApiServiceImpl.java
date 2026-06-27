package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.domain.service.CouponDomainService;
import com.marcinferenc.emp.backend.rest.converter.CouponClaimRequestRestConverter;
import com.marcinferenc.emp.backend.rest.converter.CouponClaimResponseRestConverter;
import com.marcinferenc.emp.backend.rest.converter.CouponCreationRequestRestConverter;
import com.marcinferenc.emp.backend.rest.converter.CouponCreationResponseRestConverter;
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
    private final CouponDomainService couponDomainService;

    private final CouponClaimRequestRestConverter couponClaimRequestRestConverter;
    private final CouponClaimResponseRestConverter couponClaimResponseRestConverter;

    private final CouponCreationRequestRestConverter couponCreationRequestRestConverter;
    private final CouponCreationResponseRestConverter couponCreationResponseRestConverter;


    @Override
    public CouponClaimResponseDTO claim(CouponClaimRequestDTO couponClaimRequest) {
        couponValidationService.validate(couponClaimRequest);

        CouponClaimRequestDO couponClaimRequestDO = couponClaimRequestRestConverter.toDomainObject(couponClaimRequest);
        CouponClaimResponseDO couponClaimResponseDO = couponDomainService.claim(couponClaimRequestDO);
        CouponClaimResponseDTO result = couponClaimResponseRestConverter.toDto(couponClaimResponseDO);

        log.debug("Coupon claim response: {}", result);
        return result;
    }

    @Override
    public CouponCreationResponseDTO create(CouponCreationRequestDTO couponCreationRequest) {
        couponValidationService.validate(couponCreationRequest);

        CouponCreationRequestDO couponCreationRequestDO = couponCreationRequestRestConverter.toDomainObject(couponCreationRequest);
        CouponCreationResponseDO couponCreationResponseDO = couponDomainService.create(couponCreationRequestDO);
        CouponCreationResponseDTO result = couponCreationResponseRestConverter.toDto(couponCreationResponseDO);

        log.debug("Coupon creation response: {}", result);
        return result;
    }
}
