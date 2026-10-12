package com.example.currencyconverter.service;

import com.example.currencyconverter.client.ExchangeRateClient;
import com.example.currencyconverter.dto.ConversionResponse;
import com.example.currencyconverter.exception.UnsupportedCurrencyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Service
public class CurrencyService {

    private final ExchangeRateClient client;

    public CurrencyService(ExchangeRateClient client) {
        this.client = client;
    }

    public ConversionResponse convert(String from, String to, BigDecimal amount) {
        String source = normalize(from);
        String target = normalize(to);

        Map<String, BigDecimal> rates = client.getRates(source);
        BigDecimal rate = rates.get(target);
        if (rate == null) {
            throw new UnsupportedCurrencyException(target);
        }

        BigDecimal result = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        return new ConversionResponse(source, target, amount, rate, result, Instant.now());
    }

    public Map<String, BigDecimal> getRates(String base) {
        return client.getRates(normalize(base));
    }

    public Set<String> getSupportedCurrencies() {
        return new TreeSet<>(client.getRates("USD").keySet());
    }

    private String normalize(String code) {
        return code.trim().toUpperCase(Locale.ROOT);
    }
}
