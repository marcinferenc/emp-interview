package com.marcinferenc.emp.backend;

import com.marcinferenc.emp.backend.adapter.persistence.service.CouponRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.autoconfigure.exclude="
            + "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,"
            + "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration",
        "ipinfo.api.key=test-api-key"
    }
)
class OpenApiDocumentationTest {

    @LocalServerPort private int port;

    private final HttpClient httpClient = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    @Test
    void shouldExposeOpenApiDocs() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/v3/api-docs");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"openapi\"");
        assertThat(response.body()).contains("\"/claim\"");
        assertThat(response.body()).contains("\"/create\"");
    }

    @Test
    void shouldExposeSwaggerUi() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/swagger-ui.html");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Swagger UI");
    }

    private HttpResponse<String> get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + path))
            .GET()
            .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @TestConfiguration
    static class MockPersistenceConfiguration {

        @Bean
        CouponRepository couponRepository() {
            return Mockito.mock(CouponRepository.class);
        }

        @Bean
        TransactionTemplate transactionTemplate() {
            return Mockito.mock(TransactionTemplate.class);
        }
    }
}
