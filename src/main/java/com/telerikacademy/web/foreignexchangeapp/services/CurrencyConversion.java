package com.telerikacademy.web.foreignexchangeapp.services;

import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CurrencyConversion {
    Conversion convertCurrency(BigDecimal amount, String sourceCurrency, String targetCurrency);
    Optional<Conversion> getConversionTransactionById(String transactionId);
    List<Conversion> getConversionHistory(LocalDateTime start, LocalDateTime end);
    Page<Conversion> getConversionHistory(LocalDateTime start, LocalDateTime end, Pageable pageable);
}