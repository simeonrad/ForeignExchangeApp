package com.telerikacademy.web.foreignexchangeapp.controllers;

import com.telerikacademy.web.foreignexchangeapp.models.ExchangeRate;
import com.telerikacademy.web.foreignexchangeapp.services.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("{sourceCurrency}/{targetCurrency}")
    public ResponseEntity<ExchangeRate> exchangeRates(@PathVariable String sourceCurrency, @PathVariable String targetCurrency) {
        BigDecimal rate = exchangeRateService.fetchCurrentExchangeRate(sourceCurrency, targetCurrency);
        ExchangeRate response = new ExchangeRate(sourceCurrency, targetCurrency, rate);
        return ResponseEntity.ok(response);
    }
}
