package com.example.currencyconverter.exception;

public class UnsupportedCurrencyException extends RuntimeException {
    public UnsupportedCurrencyException(String code) {
        super("Unsupported or unknown currency code: " + code);
    }
}
