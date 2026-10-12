package com.example.currencyconverter.controller;

import com.example.currencyconverter.dto.ConversionResponse;
import com.example.currencyconverter.service.CurrencyService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
@Validated
public class CurrencyController {

    private static final String CODE_REGEX = "^[A-Za-z]{3}$";
    private static final String CODE_MSG = "must be a 3-letter currency code";

    private final CurrencyService service;

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    /** GET /api/convert?from=USD&to=INR&amount=100 */
    @GetMapping("/convert")
    public ConversionResponse convert(
            @RequestParam @Pattern(regexp = CODE_REGEX, message = CODE_MSG) String from,
            @RequestParam @Pattern(regexp = CODE_REGEX, message = CODE_MSG) String to,
            @RequestParam @NotNull @DecimalMin(value = "0.0", inclusive = false,
                    message = "must be greater than 0") BigDecimal amount) {
        return service.convert(from, to, amount);
    }

    /** GET /api/rates/USD */
    @GetMapping("/rates/{base}")
    public Map<String, BigDecimal> rates(
            @PathVariable @Pattern(regexp = CODE_REGEX, message = CODE_MSG) String base) {
        return service.getRates(base);
    }

    /** GET /api/currencies */
    @GetMapping("/currencies")
    public Set<String> currencies() {
        return service.getSupportedCurrencies();
    }
}
