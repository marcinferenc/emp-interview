package com.marcinferenc.emp.backend.port;

public interface IpInfoPort {
    boolean isIpCountryValid(String ipAddress, String countryCode);
}
