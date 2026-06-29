package com.marcinferenc.emp.backend.adapter.ipinfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class IpInfoServiceImpl implements IpInfoService {
    private static final Pattern COUNTRY_CODE_PATTERN = Pattern.compile("\"country_code\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern COUNTRY_CODE_CAMEL_CASE_PATTERN = Pattern.compile("\"countryCode\"\\s*:\\s*\"([^\"]+)\"");

    private final IpAddressTransformationService ipAddressTransformationService;
    private final CloseableHttpClient httpClient;
    private final String apiKey;
    private final String apiUrl;

    @Autowired
    public IpInfoServiceImpl(
        @Value("${ipinfo.api.key}") String apiKey,
        @Value("${ipinfo.api.url}") String apiUrl,
        IpAddressTransformationService ipAddressTransformationService
    ) {
        this(apiKey, apiUrl, HttpClientBuilder.create().build(), ipAddressTransformationService);
    }

    IpInfoServiceImpl(
        String apiKey,
        String apiUrl,
        CloseableHttpClient httpClient,
        IpAddressTransformationService ipAddressTransformationService
    ) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.httpClient = httpClient;
        this.ipAddressTransformationService = ipAddressTransformationService;
    }

    @Override
    public String getCountryCode(String ipAddress) {
        String transformedIpAddress = ipAddressTransformationService.transform(ipAddress);
        return getCountryCodeInternal(transformedIpAddress);
    }

    public String getCountryCodeInternal(String ipAddress) {
        HttpGet request = new HttpGet(buildRequestUrl(ipAddress));

        try {
            return httpClient.execute(request, response -> {
                HttpEntity entity = response.getEntity();
                String responseBody = entity == null ? "" : EntityUtils.toString(entity);

                if (response.getCode() < HttpStatus.SC_SUCCESS || response.getCode() >= HttpStatus.SC_REDIRECTION) {
                    throw new IllegalStateException("IpInfo API request failed with status " + response.getCode());
                }

                String countryCode = extractCountryCode(responseBody);
                if (countryCode == null || countryCode.isBlank()) {
                    throw new IllegalStateException("IpInfo API response does not contain country code");
                }

                return countryCode;
            });
        } catch (IOException e) {
            throw new IllegalStateException("IpInfo API request failed", e);
        }
    }

    private String buildRequestUrl(String ipAddress) {
        String baseUrl = apiUrl.endsWith("/") ? apiUrl : apiUrl + "/";
        return baseUrl
            + URLEncoder.encode(ipAddress, StandardCharsets.UTF_8)
            + "?token="
            + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
    }

    private String extractCountryCode(String responseBody) {
        Matcher snakeCaseMatcher = COUNTRY_CODE_PATTERN.matcher(responseBody);
        if (snakeCaseMatcher.find()) {
            return snakeCaseMatcher.group(1);
        }

        Matcher camelCaseMatcher = COUNTRY_CODE_CAMEL_CASE_PATTERN.matcher(responseBody);
        if (camelCaseMatcher.find()) {
            return camelCaseMatcher.group(1);
        }

        return null;
    }
}
