package com.marcinferenc.emp.backend.adapter.ipinfo;

import com.marcinferenc.emp.backend.port.IpInfoPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IpInfoAdapterPort implements IpInfoPort {
    public static final String COUNTRY_CODE_CACHE = "ipInfoCountryCode";

    private final IpInfoService ipInfoService;
    private final IpAddressOverrideService ipAddressOverrideService;

    @Override
    @Cacheable(cacheNames = COUNTRY_CODE_CACHE)
    public String getCountryCode(String ipAddress) {
        String overriddenIpAddress = ipAddressOverrideService.override(ipAddress);
        return ipInfoService.getCountryCode(overriddenIpAddress);
    }
}
