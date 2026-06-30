package com.marcinferenc.emp.backend.apiTest;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import com.marcinferenc.emp.backend.adapter.persistence.service.CouponRepository;
import com.marcinferenc.emp.backend.rest.model.CouponCreationRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponApiTest {
    private static final int OPERATION_TIMEOUT_SECONDS = 3;
    private static final Random random = new SecureRandom();

    @LocalServerPort private int port;

    public static final int COUPON_AMOUNT = 10_000;
    public static final String COUNTRY_CODE_POLAND = "PL";
    public static final String COUNTRY_CODE_GERMANY = "DE";
    public static final String COUPON_CODE = "api-test-coupon";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    @Autowired private CouponRepository couponRepository;

    @AfterEach
    void closeHttpClient() {
        httpClient.close();
    }

    @BeforeEach
    void deleteCoupons() {
        couponRepository.deleteAll();
        couponRepository.flush();
    }

    @Test
    void shouldCreateCouponOverHttp() throws Exception {
        CouponCreationRequestDTO couponCreationRequestDTO = createCouponRequest(1, COUNTRY_CODE_POLAND);
        HttpResponse<String> response = createCoupon(couponCreationRequestDTO);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"message\":\"Coupon created OK:");

        List<CouponBO> coupons = couponRepository.findAll();
        assertThat(coupons).hasSize(1);
        CouponBO coupon = coupons.getFirst();
        assertThat(coupon.getCouponCode()).isEqualTo(couponCode(1));
        assertThat(coupon.getCountryCode()).isEqualTo(COUNTRY_CODE_POLAND);
        assertThat(coupon.getClaimLimitCount()).isEqualTo(1);
        assertThat(coupon.getClaimCount()).isZero();
    }

    @Test
    void shouldCreateCouponsConcurrentlyOverHttp() throws Exception {
        List<CouponCreationRequestDTO> couponCreationRequestDTOS = IntStream.rangeClosed(1, COUPON_AMOUNT)
            .boxed()
            .map(couponNumber -> createCouponRequest(couponNumber, COUNTRY_CODE_GERMANY))
            .toList();

        List<Callable<HttpResponse<String>>> tasks = couponCreationRequestDTOS.stream()
            .map(
                couponCreationRequestDTO ->
                    (Callable<HttpResponse<String>>) () -> createCoupon(couponCreationRequestDTO))
            .toList();

        try (ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            List<Future<HttpResponse<String>>> futures = tasks.stream()
                .map(executorService::submit)
                .toList();

            for (Future<HttpResponse<String>> future : futures) {
                HttpResponse<String> response = future.get(OPERATION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                assertThat(response.statusCode()).isEqualTo(200);
                assertThat(response.body()).contains("\"message\":\"Coupon created OK:");
            }
        }

        List<CouponBO> coupons = couponRepository.findAll();
        assertThat(coupons).hasSize(COUPON_AMOUNT);
        assertThat(coupons).allSatisfy(coupon -> {
            CouponCreationRequestDTO matchedRequest = couponCreationRequestDTOS.stream()
                .filter(dto -> dto.getCouponCode().equals(coupon.getCouponCode()))
                .findFirst().orElseThrow();

            assertThat(coupon.getCountryCode()).isEqualTo(COUNTRY_CODE_GERMANY);
            assertThat(coupon.getClaimLimitCount()).isEqualTo(matchedRequest.getClaimLimitCount());
            assertThat(coupon.getClaimCount()).isZero();
        });
        assertThat(coupons.stream().map(CouponBO::getCouponCode).collect(java.util.stream.Collectors.toSet()))
            .isEqualTo(expectedCouponCodes());
    }

    private CouponCreationRequestDTO createCouponRequest(int couponNumber, String countryCode) {
        CouponCreationRequestDTO couponCreationRequestDTO = new CouponCreationRequestDTO();
        couponCreationRequestDTO.setCouponCode(couponCode(couponNumber));
        couponCreationRequestDTO.setCountryCode(countryCode);
        couponCreationRequestDTO.setClaimLimitCount(random.nextInt(1000) + 1);
        return couponCreationRequestDTO;
    }

    private HttpResponse<String> createCoupon(CouponCreationRequestDTO couponRequest) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + "/create"))
            .timeout(Duration.ofSeconds(OPERATION_TIMEOUT_SECONDS))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(toJson(couponRequest)))
            .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String toJson(CouponCreationRequestDTO couponRequest) {
        return """
            {
              "couponCode": "%s",
              "countryCode": "%s",
              "claimLimitCount": %d
            }
            """.formatted(
            escapeJson(couponRequest.getCouponCode()),
            escapeJson(couponRequest.getCountryCode()),
            couponRequest.getClaimLimitCount()
        );
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private Set<String> expectedCouponCodes() {
        Set<String> couponCodes = new LinkedHashSet<>();
        for (int couponNumber = 1; couponNumber <= COUPON_AMOUNT; couponNumber++) {
            couponCodes.add(couponCode(couponNumber));
        }
        return couponCodes;
    }

    private String couponCode(int couponNumber) {
        return "%s_%d".formatted(COUPON_CODE, couponNumber);
    }
}
