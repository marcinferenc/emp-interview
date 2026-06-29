package com.marcinferenc.emp.backend.adapter.persistence.service;

import java.util.concurrent.TimeUnit;

public interface TestConfig {
    //generation
    String COUNTRY_CODE = "US";
    int COUPON_COUNT = 100;
    int COUPON_CODE_LENGTH = 2;
    int COUPON_CLAIM_LIMIT_COUNT = 50;

    //timeout
    long TIMEOUT_SECONDS = 1;
    TimeUnit TIMEOUT_TIME_UNIT = TimeUnit.SECONDS;
}
