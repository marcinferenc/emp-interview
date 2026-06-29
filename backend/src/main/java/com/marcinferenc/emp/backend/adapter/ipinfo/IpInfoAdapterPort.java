package com.marcinferenc.emp.backend.adapter.ipinfo;

import com.marcinferenc.emp.backend.port.IpInfoPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IpInfoAdapterPort implements IpInfoPort {
    @Override
    public String ipAddressToCountryCode(String ipAddress) {
        //FIXME MOCKED
        return "pl";
    }
}
