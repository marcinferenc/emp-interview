package com.marcinferenc.emp.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CouponBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponBackendApplication.class, args);
		log.info("application started - 3");
	}

}
