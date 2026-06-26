package com.marcinferenc.emp.backend.adapter.ipinfo;

import com.marcinferenc.emp.backend.port.IpInfoPort;

public class IpInfoAdapterPort implements IpInfoPort {
    @Override
    public boolean isIpCountryValid(String ipAddress, String countryCode) {
        return true;
    }
}
