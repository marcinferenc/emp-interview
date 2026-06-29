package com.marcinferenc.emp.backend.adapter.ipinfo;

import com.marcinferenc.emp.backend.rest.ErrorCode;
import com.marcinferenc.emp.backend.rest.model.CouponException;
import com.sun.net.httpserver.HttpServer;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IpInfoServiceImplTest {

    @Test
    void shouldRequestIpInfoApiAndReturnCountryCode() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        AtomicReference<String> requestPath = new AtomicReference<>();
        AtomicReference<String> requestQuery = new AtomicReference<>();
        String ipAddressOverride = "203.0.113.10";
        server.createContext("/lite/" + ipAddressOverride, exchange -> {
            requestPath.set(exchange.getRequestURI().getPath());
            requestQuery.set(exchange.getRequestURI().getQuery());
            byte[] response = "{\"country_code\":\"PL\"}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });
        server.start();

        try {
            String apiUrl = "http://localhost:" + server.getAddress().getPort() + "/lite/";
            IpInfoServiceImpl service = new IpInfoServiceImpl(
                "49ru49454o4fvh",
                apiUrl,
                HttpClientBuilder.create().build(),
                new IpAddressDevOverrideServiceImpl(ipAddressOverride)
            );

            String countryCode = service.getCountryCode("8.8.8.8");

            assertThat(countryCode).isEqualTo("PL");
            assertThat(requestPath.get()).isEqualTo("/lite/" + ipAddressOverride);
            assertThat(requestQuery.get()).isEqualTo("token=49ru49454o4fvh");
        } finally {
            server.stop(0);
        }
    }

    @Test
    void shouldThrowCountryCodeUnknownWhenCountryCodeIsMissingForLocalhost() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        String ipAddress = "127.0.0.1";
        server.createContext("/lite/" + ipAddress, exchange -> {
            byte[] response = "{}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });
        server.start();

        try {
            IpInfoServiceImpl service = createService(server);

            assertThatThrownBy(() -> service.getCountryCode(ipAddress))
                .isInstanceOf(CouponException.class)
                .hasMessage("Country code not found in response")
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COUNTRY_CODE_UNKNOWN);
        } finally {
            server.stop(0);
        }
    }

    @Test
    void shouldThrowCountryCodeUnknownWhenUnableToDetermineCountryCodeForInvalidIp() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        String invalidIpAddress = "invalid-ip";
        server.createContext("/lite/" + invalidIpAddress, exchange -> {
            byte[] response = "{\"error\":\"Unable to determine country code\"}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(400, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });
        server.start();

        try {
            IpInfoServiceImpl service = createService(server);

            assertThatThrownBy(() -> service.getCountryCode(invalidIpAddress))
                .isInstanceOf(CouponException.class)
                .hasMessageContaining("Unable to determine country code")
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COUNTRY_CODE_UNKNOWN);
        } finally {
            server.stop(0);
        }
    }

    @Test
    void shouldTimeoutWhenIpInfoApiResponseTakesLongerThanSixSeconds() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        String ipAddress = "8.8.8.8";
        server.createContext("/lite/" + ipAddress, exchange -> {
            try {
                Thread.sleep(Duration.ofSeconds(6));
                byte[] response = "{\"country_code\":\"PL\"}".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, response.length);
                exchange.getResponseBody().write(response);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                exchange.close();
            }
        });
        server.start();

        try {
            IpInfoServiceImpl service = new IpInfoServiceImpl(
                "49ru49454o4fvh",
                createApiUrl(server),
                new IpAddressOverrideServiceImpl()
            );

            assertThatThrownBy(() -> service.getCountryCode(ipAddress))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("IpInfo API request failed")
                .hasCauseInstanceOf(InterruptedIOException.class);
        } finally {
            server.stop(0);
        }
    }

    private IpInfoServiceImpl createService(HttpServer server) {
        return new IpInfoServiceImpl(
            "49ru49454o4fvh",
            createApiUrl(server),
            HttpClientBuilder.create().build(),
            new IpAddressOverrideServiceImpl()
        );
    }

    private String createApiUrl(HttpServer server) {
        return "http://localhost:" + server.getAddress().getPort() + "/lite/";
    }

}
