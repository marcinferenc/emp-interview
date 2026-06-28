package com.marcinferenc.emp.backend.adapter.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "coupon")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CouponBO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String couponCode;
    String countryCode;
    Integer claimLimitCount;
    Integer claimCount;
    Instant createdAt;
}
