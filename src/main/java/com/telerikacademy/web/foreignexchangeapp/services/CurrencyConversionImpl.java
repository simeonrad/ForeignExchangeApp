package com.telerikacademy.web.foreignexchangeapp.services;

import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CurrencyConversionImpl implements CurrencyConversion{
    @Override
    public Conversion convertCurrency(BigDecimal amount, String sourceCurrency, String targetCurrency) {
        BigDecimal rate = exchangeRateService.fetchCurrentExchangeRate(sourceCurrency, targetCurrency);
        BigDecimal convertedAmount = amount.multiply(rate);

        Conversion conversion = new Conversion();
        conversion.setSourceAmount(amount);
        conversion.setSourceCurrency(sourceCurrency);
        conversion.setTargetCurrency(targetCurrency);
        conversion.setTargetAmount(convertedAmount);
        conversion.setExchangeRate(rate);
        conversion.setConversionTime(LocalDateTime.now());
        conversion.setTransactionId(UUID.randomUUID().toString());

        return conversionRepository.save(conversion);    }

    @Override
    public Optional<Conversion> getConversionTransactionById(String transactionId) {
        return conversionRepository.findByTransactionId(transactionId);
    }

    @Override
    public List<Conversion> getConversionHistory(LocalDateTime start, LocalDateTime end) {
        return conversionRepository.findByConversionTimeBetween(start, end);
    }

    @Override
    public Page<Conversion> getConversionHistory(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return conversionRepository.findByConversionTimeBetween(start, end, pageable);
    }
}
