package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.domain.model.CouponClaimRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponClaimResponseDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationRequestDO;
import com.marcinferenc.emp.backend.domain.model.CouponCreationResponseDO;
import com.marcinferenc.emp.backend.domain.service.CouponDomainService;
import com.marcinferenc.emp.backend.rest.converter.CouponClaimRequestConverter;
import com.marcinferenc.emp.backend.rest.converter.CouponClaimResponseConverter;
import com.marcinferenc.emp.backend.rest.converter.CouponCreationRequestConverter;
import com.marcinferenc.emp.backend.rest.converter.CouponCreationResponseConverter;
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

    private final CouponClaimRequestConverter couponClaimRequestConverter;
    private final CouponClaimResponseConverter couponClaimResponseConverter;

    private final CouponCreationRequestConverter couponCreationRequestConverter;
    private final CouponCreationResponseConverter couponCreationResponseConverter;


    @Override
    public CouponClaimResponseDTO claim(CouponClaimRequestDTO couponClaimRequest) {
        couponValidationService.validate(couponClaimRequest);

        CouponClaimRequestDO couponClaimRequestDO = couponClaimRequestConverter.toDomainObject(couponClaimRequest);
        CouponClaimResponseDO couponClaimResponseDO = couponDomainService.claim(couponClaimRequestDO);
        CouponClaimResponseDTO result = couponClaimResponseConverter.toDto(couponClaimResponseDO);

        log.debug("Coupon claim response: {}", result);
        return result;
    }

    @Override
    public CouponCreationResponseDTO create(CouponCreationRequestDTO couponCreationRequest) {
        couponValidationService.validate(couponCreationRequest);

        CouponCreationRequestDO couponCreationRequestDO = couponCreationRequestConverter.toDomainObject(couponCreationRequest);
        CouponCreationResponseDO couponCreationResponseDO = couponDomainService.create(couponCreationRequestDO);
        CouponCreationResponseDTO result = couponCreationResponseConverter.toDto(couponCreationResponseDO);

        log.debug("Coupon creation response: {}", result);
        return result;
    }
}
