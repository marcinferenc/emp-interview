package com.marcinferenc.emp.backend.adapter.ipinfo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!dev")
public class IpAddressOverrideServiceImpl implements IpAddressOverrideService {
    @Override
    public String override(String ipAddress) {
        return ipAddress;
    }
}
