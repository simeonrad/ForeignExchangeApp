package com.telerikacademy.web.foreignexchangeapp.services;

import com.telerikacademy.web.foreignexchangeapp.models.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRateService {
    BigDecimal fetchCurrentExchangeRate(String sourceCurrency, String targetCurrency);

    void updateExchangeRates();

    List<ExchangeRate> findExchangeRatesBetween(LocalDateTime start, LocalDateTime end);
}
