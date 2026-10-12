package com.example.currencyconverter.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ConversionResponse(
        String from,
        String to,
        BigDecimal amount,
        BigDecimal rate,
        BigDecimal result,
        Instant timestamp) {
}
