package com.marcinferenc.emp.backend.adapter.ipinfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class IpAddressDevTransformationServiceImpl implements IpAddressTransformationService {
    private final String ipAddressOverride;

    public IpAddressDevTransformationServiceImpl(
        @Value("${ipinfo.ip.address.override}") String ipAddressOverride
    ) {
        this.ipAddressOverride = ipAddressOverride;
    }

    @Override
    public String transform(String ipAddress) {
        return ipAddressOverride;
    }
}
