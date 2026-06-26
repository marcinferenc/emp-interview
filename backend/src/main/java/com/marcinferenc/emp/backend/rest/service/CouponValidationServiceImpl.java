package com.marcinferenc.emp.backend.rest.service;

import com.marcinferenc.emp.backend.rest.ErrorCode;
import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CouponValidationServiceImpl implements CouponValidationService {
    private static final Set<String> ISO_COUNTRY_CODES = Arrays.stream(Locale.getISOCountries())
        .collect(Collectors.toUnmodifiableSet());

    @Override
    public void validate(CouponCreationRequestDTO creationRequest) {
        if (creationRequest == null) {
            throwValidationError("Coupon creation request is required");
        }

        if (!hasLetterOrDigit(creationRequest.getCouponCode())) {
            throwValidationError("Coupon code must contain at least one letter or digit");
        }

        if (!isIsoCountryCode(creationRequest.getCountryCode())) {
            throwValidationError("Country code must be a valid ISO country code");
        }

        if (!isValidClaimLimitCount(creationRequest.getClaimLimitCount())) {
            throwValidationError("Claim limit count must be between 1 and Integer.MAX_VALUE");
        }
    }

    @Override
    public void validate(CouponClaimRequestDTO claimRequest) {

    }

    private boolean hasLetterOrDigit(String couponCode) {
        return couponCode != null && couponCode.chars().anyMatch(Character::isLetterOrDigit);
    }

    private boolean isIsoCountryCode(String countryCode) {
        return countryCode != null && ISO_COUNTRY_CODES.contains(countryCode.toUpperCase(Locale.ROOT));
    }

    private boolean isValidClaimLimitCount(Integer claimLimitCount) {
        return claimLimitCount != null && claimLimitCount >= 1;
    }

    private void throwValidationError(String message) {
        throw new CouponException(ErrorCode.VALIDATION_ERROR, message);
    }
}
