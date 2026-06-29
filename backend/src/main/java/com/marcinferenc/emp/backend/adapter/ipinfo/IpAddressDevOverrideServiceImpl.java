package com.marcinferenc.emp.backend.adapter.ipinfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class IpAddressDevOverrideServiceImpl implements IpAddressOverrideService {
    private final String ipAddressOverride;

    public IpAddressDevOverrideServiceImpl(
        @Value("${ipinfo.ip.address.override}") String ipAddressOverride
    ) {
        this.ipAddressOverride = ipAddressOverride;
    }

    @Override
    public String override(String ipAddress) {
        return ipAddressOverride;
    }
}
