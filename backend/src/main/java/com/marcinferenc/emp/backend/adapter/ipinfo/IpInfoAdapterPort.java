package com.marcinferenc.emp.backend.adapter.ipinfo;

import com.marcinferenc.emp.backend.port.IpInfoPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IpInfoAdapterPort implements IpInfoPort {
    private final IpInfoService ipInfoService;
    private final IpAddressTransformationService ipAddressTransformationService;

    @Override
    public String getCountryCode(String ipAddress) {
        return ipInfoService.getCountryCode(ipAddress);
    }
}
