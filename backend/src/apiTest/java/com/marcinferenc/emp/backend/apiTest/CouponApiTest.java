package com.marcinferenc.emp.backend.apiTest;

import com.marcinferenc.emp.backend.adapter.persistence.model.CouponBO;
import com.marcinferenc.emp.backend.adapter.persistence.service.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void deleteCoupons() {
        couponRepository.deleteAll();
        couponRepository.flush();
    }

    @Test
    void shouldCreateCouponOverHttp() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + "/create"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("""
                {
                  "couponCode": "api-test-coupon",
                  "countryCode": "PL",
                  "claimLimitCount": 1
                }
                """))
            .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"message\":\"Coupon created OK:");

        List<CouponBO> coupons = couponRepository.findAll();
        assertThat(coupons).hasSize(1);
        CouponBO coupon = coupons.getFirst();
        assertThat(coupon.getCouponCode()).isEqualTo("api-test-coupon");
        assertThat(coupon.getCountryCode()).isEqualTo("PL");
        assertThat(coupon.getClaimLimitCount()).isEqualTo(1);
        assertThat(coupon.getClaimCount()).isZero();
    }
}
