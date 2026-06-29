package com.marcinferenc.emp.backend.adapter.ipinfo;

import com.sun.net.httpserver.HttpServer;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

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
                new IpAddressDevTransformationServiceImpl(ipAddressOverride)
            );

            String countryCode = service.getCountryCode("8.8.8.8");

            assertThat(countryCode).isEqualTo("PL");
            assertThat(requestPath.get()).isEqualTo("/lite/" + ipAddressOverride);
            assertThat(requestQuery.get()).isEqualTo("token=49ru49454o4fvh");
        } finally {
            server.stop(0);
        }
    }
}
