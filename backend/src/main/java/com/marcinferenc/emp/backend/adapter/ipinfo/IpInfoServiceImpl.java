package com.marcinferenc.emp.backend.adapter.ipinfo;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IpInfoServiceImpl implements IpInfoService {
    private static final HttpClient httpClient = HttpClientBuilder.create()
        .build();

    @Override
    public String ipAddressToCountryCode(String ipAddress) {
        return "pl";
    }
}
