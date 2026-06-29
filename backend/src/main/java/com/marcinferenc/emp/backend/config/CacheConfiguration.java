package com.marcinferenc.emp.backend.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.marcinferenc.emp.backend.adapter.ipinfo.IpInfoAdapterPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Locale;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    CacheManager cacheManager(
        @Value("${ipinfo.cache.maximum-size:1000}") long ipInfoCountryCodeMaximumSize,
        @Value("${ipinfo.cache.ttl:1m}") String ipInfoCountryCodeTtl
    ) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(IpInfoAdapterPort.COUNTRY_CODE_CACHE);
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(ipInfoCountryCodeMaximumSize)
            .expireAfterWrite(parseDuration(ipInfoCountryCodeTtl)));
        return cacheManager;
    }

    private Duration parseDuration(String value) {
        String normalizedValue = value.trim().toLowerCase(Locale.ROOT);
        if (normalizedValue.endsWith("ms")) {
            return Duration.ofMillis(Long.parseLong(normalizedValue.substring(0, normalizedValue.length() - 2)));
        }
        if (normalizedValue.endsWith("s")) {
            return Duration.ofSeconds(Long.parseLong(normalizedValue.substring(0, normalizedValue.length() - 1)));
        }
        if (normalizedValue.endsWith("m")) {
            return Duration.ofMinutes(Long.parseLong(normalizedValue.substring(0, normalizedValue.length() - 1)));
        }
        return Duration.parse(value);
    }
}
