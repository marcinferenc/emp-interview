package com.marcinferenc.emp.backend.adapter.ipinfo;

import com.marcinferenc.emp.backend.config.CacheConfiguration;
import com.marcinferenc.emp.backend.port.IpInfoPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@ContextConfiguration(classes = {
    IpInfoAdapterPort.class,
    CacheConfiguration.class,
    IpInfoAdapterPortCacheTest.TestConfiguration.class
})
@TestPropertySource(properties = {
    "ipinfo.cache.maximum-size=1000",
    "ipinfo.cache.ttl=5s"
})
class IpInfoAdapterPortCacheTest {

    @Autowired private IpInfoPort ipInfoPort;
    @Autowired private IpInfoService ipInfoService;
    @Autowired private IpAddressOverrideService ipAddressOverrideService;
    @Autowired private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager.getCache(IpInfoAdapterPort.COUNTRY_CODE_CACHE).clear();
        reset(ipInfoService, ipAddressOverrideService);
    }

    @Configuration
    static class TestConfiguration {
        @Bean IpInfoService ipInfoService() {
            return mock(IpInfoService.class);
        }
        @Bean IpAddressOverrideService ipAddressOverrideService() {
            return mock(IpAddressOverrideService.class);
        }
    }

    @Test
    void shouldCacheCountryCodeForIpAddress() {
        when(ipAddressOverrideService.override("8.8.8.8")).thenReturn("203.0.113.10");
        when(ipInfoService.getCountryCode("203.0.113.10")).thenReturn("PL");

        String firstCountryCode = ipInfoPort.getCountryCode("8.8.8.8");
        String secondCountryCode = ipInfoPort.getCountryCode("8.8.8.8");

        assertThat(firstCountryCode).isEqualTo("PL");
        assertThat(secondCountryCode).isEqualTo("PL");
        verify(ipAddressOverrideService, times(1)).override("8.8.8.8");
        verify(ipInfoService, times(1)).getCountryCode("203.0.113.10");
    }

    @Test
    void shouldExpireCountryCodeCacheAfterConfiguredTtl() throws InterruptedException {
        when(ipAddressOverrideService.override("8.8.8.8")).thenReturn("203.0.113.10");
        when(ipInfoService.getCountryCode("203.0.113.10")).thenReturn("PL", "DE");

        String firstCountryCode = ipInfoPort.getCountryCode("8.8.8.8");
        //more than 5s
        Thread.sleep(5100);
        String secondCountryCode = ipInfoPort.getCountryCode("8.8.8.8");

        assertThat(firstCountryCode).isEqualTo("PL");
        assertThat(secondCountryCode).isEqualTo("DE");
        verify(ipAddressOverrideService, times(2)).override("8.8.8.8");
        verify(ipInfoService, times(2)).getCountryCode("203.0.113.10");
    }
}
