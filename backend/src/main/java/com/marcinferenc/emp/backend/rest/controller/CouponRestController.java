package com.marcinferenc.emp.backend.rest.controller;

import com.marcinferenc.emp.backend.rest.model.CouponClaimRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponClaimResponseDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import com.marcinferenc.emp.backend.rest.model.CouponCreationResponseDTO;
import com.marcinferenc.emp.backend.rest.model.CouponErrorResponseDTO;
import com.marcinferenc.emp.backend.rest.service.CouponApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Coupons", description = "Coupon creation and claim operations")
public class CouponRestController {
    private final CouponApiService couponApiService;

    @Operation(summary = "Claim a coupon", description = "Claims an existing coupon for the provided user email address.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Coupon claimed",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CouponClaimResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid claim request",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CouponErrorResponseDTO.class),
                examples = @ExampleObject(value = "{\"errorCode\":\"VALIDATION_ERROR\",\"message\":\"Invalid coupon code\"}")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Coupon not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CouponErrorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Coupon claim limit exceeded",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CouponErrorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected server error",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CouponErrorResponseDTO.class)
            )
        )
    })
    @PostMapping(path = "/claim", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CouponClaimResponseDTO claimCoupon(@RequestBody CouponClaimRequestDTO couponClaimRequest) {
        logRequest(couponClaimRequest);
        return couponApiService.claim(couponClaimRequest);
    }

    @Operation(summary = "Create a coupon", description = "Creates a coupon for a country with a configured claim limit.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Coupon created",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CouponCreationResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid creation request",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CouponErrorResponseDTO.class),
                examples = @ExampleObject(value = "{\"errorCode\":\"VALIDATION_ERROR\",\"message\":\"Invalid country code\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected server error",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CouponErrorResponseDTO.class)
            )
        )
    })
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
