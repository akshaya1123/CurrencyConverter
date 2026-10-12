package com.example.currencyconverter.client;

import com.example.currencyconverter.config.AppConfig;
import com.example.currencyconverter.config.ExchangeApiProperties;
import com.example.currencyconverter.dto.ExchangeApiResponse;
import com.example.currencyconverter.exception.ExchangeApiException;
import com.example.currencyconverter.exception.UnsupportedCurrencyException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.Map;

/** Thin client around ExchangeRate-API. Results are cached per base currency. */
@Component
public class ExchangeRateClient {

    private final RestClient restClient;
    private final ExchangeApiProperties props;

    public ExchangeRateClient(RestClient restClient, ExchangeApiProperties props) {
        this.restClient = restClient;
        this.props = props;
    }

    @Cacheable(value = AppConfig.RATES_CACHE, key = "#base")
    public Map<String, BigDecimal> getRates(String base) {
        String url = props.hasApiKey()
                ? "%s/%s/latest/%s".formatted(props.baseUrl(), props.apiKey(), base)
                : "%s/latest/%s".formatted(props.openUrl(), base);

        ExchangeApiResponse response;
        try {
            response = restClient.get().uri(url).retrieve().body(ExchangeApiResponse.class);
        } catch (RestClientException ex) {
            throw new ExchangeApiException("Could not reach the exchange rate service", ex);
        }

        if (response == null) {
            throw new ExchangeApiException("Empty response from exchange rate service");
        }
        if (!response.isSuccess()) {
            if ("unsupported-code".equals(response.errorType())) {
                throw new UnsupportedCurrencyException(base);
            }
            throw new ExchangeApiException("Exchange rate service error: " + response.errorType());
        }
        Map<String, BigDecimal> rates = response.resolvedRates();
        if (rates == null || rates.isEmpty()) {
            throw new ExchangeApiException("Exchange rate service returned no rates");
        }
        return rates;
    }
}
