package com.telerikacademy.web.foreignexchangeapp.controllers;

import com.telerikacademy.web.foreignexchangeapp.exceptions.CurrencyNotFoundException;
import com.telerikacademy.web.foreignexchangeapp.models.ExchangeRateDTO;
import com.telerikacademy.web.foreignexchangeapp.services.ExchangeRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("{sourceCurrency}/{targetCurrency}")
    public ResponseEntity<ExchangeRateDTO> exchangeRates(@PathVariable String sourceCurrency, @PathVariable String targetCurrency) {
        try {
            BigDecimal rate = exchangeRateService.fetchCurrentExchangeRate(sourceCurrency, targetCurrency);
            ExchangeRateDTO response = new ExchangeRateDTO(sourceCurrency, targetCurrency, rate);
            return ResponseEntity.ok(response);
        } catch (CurrencyNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
