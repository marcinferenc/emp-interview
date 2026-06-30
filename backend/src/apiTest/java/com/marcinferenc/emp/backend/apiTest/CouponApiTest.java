package com.marcinferenc.emp.backend.apiTest;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import com.marcinferenc.emp.backend.adapter.persistence.service.CouponRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponApiTest {
    public static final int COUPON_AMOUNT = 100_000;
    public static final int COUPON_LIMIT = 2;
    public static final String COUNTRY_CODE = "PL";
    public static final String COUPON_CODE = "api-test-coupon";
    private static final int OPERATION_TIMEOUT_SECONDS = 3;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @LocalServerPort private int port;
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
        HttpResponse<String> response = createCoupon(1, 1);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"message\":\"Coupon created OK:");

        List<CouponBO> coupons = couponRepository.findAll();
        assertThat(coupons).hasSize(1);
        CouponBO coupon = coupons.getFirst();
        assertThat(coupon.getCouponCode()).isEqualTo(couponCode(1));
        assertThat(coupon.getCountryCode()).isEqualTo(COUNTRY_CODE);
        assertThat(coupon.getClaimLimitCount()).isEqualTo(1);
        assertThat(coupon.getClaimCount()).isZero();
    }

    @Test
    void shouldCreateCouponsConcurrentlyOverHttp() throws Exception {
        List<Callable<HttpResponse<String>>> tasks = java.util.stream.IntStream.rangeClosed(1, COUPON_AMOUNT)
            .mapToObj(couponNumber -> (Callable<HttpResponse<String>>) () -> createCoupon(couponNumber, COUPON_LIMIT))
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
            assertThat(coupon.getCountryCode()).isEqualTo(COUNTRY_CODE);
            assertThat(coupon.getClaimLimitCount()).isEqualTo(COUPON_LIMIT);
            assertThat(coupon.getClaimCount()).isZero();
        });
        assertThat(coupons.stream().map(CouponBO::getCouponCode).collect(java.util.stream.Collectors.toSet()))
            .isEqualTo(expectedCouponCodes());
    }

    private HttpResponse<String> createCoupon(int couponNumber, int couponLimit) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + "/create"))
            .timeout(Duration.ofSeconds(OPERATION_TIMEOUT_SECONDS))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("""
                {
                  "couponCode": "%s",
                  "countryCode": "%s",
                  "claimLimitCount": %d
                }
                """.formatted(couponCode(couponNumber), COUNTRY_CODE, couponLimit)))
            .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
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
