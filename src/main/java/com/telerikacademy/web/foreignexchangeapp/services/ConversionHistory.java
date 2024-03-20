package com.telerikacademy.web.foreignexchangeapp.services;

import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface ConversionHistory {
    Optional<Conversion> getConversionTransactionById(String transactionId);
    Page<Conversion> getConversionHistoryByStartEndTime(LocalDate start, LocalDate end, Pageable pageable);
    Page<Conversion> getConversionsByDate(LocalDate date, Pageable pageable);
}
