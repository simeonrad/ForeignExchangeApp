package com.telerikacademy.web.foreignexchangeapp.services;

import com.telerikacademy.web.foreignexchangeapp.models.Conversion;

import java.math.BigDecimal;

public interface CurrencyConversion {
    Conversion convertCurrency(BigDecimal amount, String sourceCurrency, String targetCurrency);
}
