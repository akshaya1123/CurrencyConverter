package com.example.currencyconverter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Maps the ExchangeRate-API "latest" response. The free open endpoint returns
 * "rates", the keyed endpoint returns "conversion_rates"; both are supported.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeApiResponse(
        String result,
        @JsonProperty("base_code") String baseCode,
        Map<String, BigDecimal> rates,
        @JsonProperty("conversion_rates") Map<String, BigDecimal> conversionRates,
        @JsonProperty("error-type") String errorType) {

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(result);
    }

    public Map<String, BigDecimal> resolvedRates() {
        if (conversionRates != null && !conversionRates.isEmpty()) {
            return conversionRates;
        }
        return rates;
    }
}
