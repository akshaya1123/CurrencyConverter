package com.example.currencyconverter.exception;

public class ExchangeApiException extends RuntimeException {
    public ExchangeApiException(String message) {
        super(message);
    }

    public ExchangeApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
