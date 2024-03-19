package com.telerikacademy.web.foreignexchangeapp.services;

import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConversionHistory {
    Optional<Conversion> getConversionTransactionById(String transactionId);

    List<Conversion> getConversionHistory(LocalDateTime start, LocalDateTime end);

    Page<Conversion> getConversionHistory(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
