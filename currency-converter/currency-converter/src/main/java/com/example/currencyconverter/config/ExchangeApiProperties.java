package com.example.currencyconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @param apiKey          ExchangeRate-API key. If blank, the free open endpoint is used.
 * @param baseUrl         Base URL used when an API key is present.
 * @param openUrl         Base URL of the free keyless endpoint.
 * @param cacheTtlMinutes How long fetched rates are cached.
 */
@ConfigurationProperties(prefix = "exchange-api")
public record ExchangeApiProperties(
        @DefaultValue("") String apiKey,
        @DefaultValue("https://v6.exchangerate-api.com/v6") String baseUrl,
        @DefaultValue("https://open.er-api.com/v6") String openUrl,
        @DefaultValue("60") long cacheTtlMinutes) {

    public boolean hasApiKey() {
        return apiKey != null && !apiKey.isBlank();
    }
}
