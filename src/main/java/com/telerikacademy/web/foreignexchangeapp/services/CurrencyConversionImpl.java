package com.telerikacademy.web.foreignexchangeapp.services;

import com.telerikacademy.web.foreignexchangeapp.exceptions.CurrencyNotFoundException;
import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import com.telerikacademy.web.foreignexchangeapp.repositories.ConversionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CurrencyConversionImpl implements CurrencyConversion{

    private final ConversionRepository conversionRepository;
    private final ExchangeRateService exchangeRateService;

    public CurrencyConversionImpl(ConversionRepository conversionRepository, ExchangeRateService exchangeRateService) {
        this.conversionRepository = conversionRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public Conversion convertCurrency(BigDecimal amount, String sourceCurrency, String targetCurrency) {
        BigDecimal rate = exchangeRateService.fetchCurrentExchangeRate(sourceCurrency, targetCurrency);

        if (rate == null) {
            throw new CurrencyNotFoundException(sourceCurrency, targetCurrency);
        }
        BigDecimal convertedAmount = amount.multiply(rate);

        Conversion conversion = new Conversion();
        conversion.setSourceAmount(amount);
        conversion.setSourceCurrency(sourceCurrency);
        conversion.setTargetCurrency(targetCurrency);
        conversion.setTargetAmount(convertedAmount);
        conversion.setExchangeRate(rate);
        conversion.setConversionTime(LocalDateTime.now());
        conversion.setTransactionId(UUID.randomUUID().toString());

        return conversionRepository.save(conversion);
    }
}
