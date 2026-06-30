UPDATE coupon
SET country_code = UPPER(country_code)
WHERE country_code IS NOT NULL;
