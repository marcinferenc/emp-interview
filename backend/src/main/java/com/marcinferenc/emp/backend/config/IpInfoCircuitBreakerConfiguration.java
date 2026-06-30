package com.marcinferenc.emp.backend.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class IpInfoCircuitBreakerConfiguration {

    @Bean
    CircuitBreaker ipInfoCircuitBreaker(
        @Value("${ipinfo.circuit-breaker.failure-rate-threshold:50}") float failureRateThreshold,
        @Value("${ipinfo.circuit-breaker.sliding-window-size:10}") int slidingWindowSize,
        @Value("${ipinfo.circuit-breaker.minimum-number-of-calls:5}") int minimumNumberOfCalls,
        @Value("${ipinfo.circuit-breaker.wait-duration-in-open-state:30s}") Duration waitDurationInOpenState,
        @Value("${ipinfo.circuit-breaker.permitted-number-of-calls-in-half-open-state:2}") int permittedNumberOfCallsInHalfOpenState
    ) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .failureRateThreshold(failureRateThreshold)
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(slidingWindowSize)
            .minimumNumberOfCalls(minimumNumberOfCalls)
            .waitDurationInOpenState(waitDurationInOpenState)
            .permittedNumberOfCallsInHalfOpenState(permittedNumberOfCallsInHalfOpenState)
            .build();

        return CircuitBreaker.of("ipInfo", config);
    }
}
