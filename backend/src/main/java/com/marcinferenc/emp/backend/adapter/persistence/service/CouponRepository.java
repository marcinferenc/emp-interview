package com.marcinferenc.emp.backend.adapter.persistence.service;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<CouponBO, Long> {
    Optional<CouponBO> findByCouponCode(String couponCode);
    Optional<CouponBO> findByCouponCodeAndCountryCode(String couponCode, String countryCode);

    @Modifying
    @Query("""
        update CouponBO c
        set c.claimCount = c.claimCount + 1
        where c.couponCode = :couponCode
          and c.claimCount < c.claimLimitCount
        """)
    int incrementClaimCountIfBelowLimit(@Param("couponCode") String couponCode);
}
