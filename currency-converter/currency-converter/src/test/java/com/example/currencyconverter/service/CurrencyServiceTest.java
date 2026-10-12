package com.example.currencyconverter.service;

import com.example.currencyconverter.client.ExchangeRateClient;
import com.example.currencyconverter.dto.ConversionResponse;
import com.example.currencyconverter.exception.UnsupportedCurrencyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    ExchangeRateClient client;

    @InjectMocks
    CurrencyService service;

    @Test
    void convertsUsingRateAndRoundsToTwoDecimals() {
        when(client.getRates("USD")).thenReturn(Map.of("INR", new BigDecimal("83.12345")));

        ConversionResponse r = service.convert("usd", "inr", new BigDecimal("10"));

        assertEquals("USD", r.from());
        assertEquals("INR", r.to());
        assertEquals(new BigDecimal("831.23"), r.result());
    }

    @Test
    void throwsWhenTargetCurrencyMissing() {
        when(client.getRates("USD")).thenReturn(Map.of("EUR", BigDecimal.ONE));

        assertThrows(UnsupportedCurrencyException.class,
                () -> service.convert("USD", "XXX", BigDecimal.TEN));
    }
}
