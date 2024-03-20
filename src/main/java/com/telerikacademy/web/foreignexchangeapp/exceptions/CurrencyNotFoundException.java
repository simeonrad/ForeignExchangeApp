package com.telerikacademy.web.foreignexchangeapp.exceptions;

public class CurrencyNotFoundException extends RuntimeException {

    public static final String PROVIDED_TARGET_ERROR = "The target currency code {%s} you provided is not valid!";
    public static final String PROVIDED_TARGET_SOURCE_ERROR = "Either the target currency code {%s} or the source currency code {%s}, or both you provided are not valid!";

    public CurrencyNotFoundException(String targetCurrency) {
        super(String.format(PROVIDED_TARGET_ERROR, targetCurrency));
    }

    public CurrencyNotFoundException(String sourceCurrency, String targetCurrency) {
        super(String.format(PROVIDED_TARGET_SOURCE_ERROR, targetCurrency, sourceCurrency));
    }
}
