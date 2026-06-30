package com.marcinferenc.emp.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Coupon Service API",
		version = "1.0.0",
		description = "REST API for creating and claiming coupons"
	)
)
@Slf4j
public class CouponBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponBackendApplication.class, args);
		log.info("application started - 3");
	}

}
