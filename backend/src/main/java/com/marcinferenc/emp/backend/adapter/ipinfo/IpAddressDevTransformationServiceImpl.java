package com.marcinferenc.emp.backend.adapter.ipinfo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class IpAddressDevTransformationServiceImpl implements IpAddressTransformationService {
    @Override
    public String transform(String ipAddress) {
        return "188.122.0.88";
    }
}
