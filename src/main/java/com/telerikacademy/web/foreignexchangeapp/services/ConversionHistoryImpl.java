package com.telerikacademy.web.foreignexchangeapp.services;

import com.telerikacademy.web.foreignexchangeapp.exceptions.NoTransactionsFoundException;
import com.telerikacademy.web.foreignexchangeapp.exceptions.TransactionNotFoundException;
import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import com.telerikacademy.web.foreignexchangeapp.repositories.ConversionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class ConversionHistoryImpl implements ConversionHistory {
    private final ConversionRepository conversionRepository;

    public ConversionHistoryImpl(ConversionRepository conversionRepository) {
        this.conversionRepository = conversionRepository;
    }

    @Override
    public Optional<Conversion> getConversionTransactionById(String transactionId) {
        if (conversionRepository.findByTransactionId(transactionId).isEmpty()) {
            throw new TransactionNotFoundException(transactionId);
        }
        return conversionRepository.findByTransactionId(transactionId);
    }

    @Override
    public Page<Conversion> getConversionsByDate(LocalDate date, Pageable pageable) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        if (conversionRepository.findByConversionTimeBetween(startOfDay, endOfDay, pageable).isEmpty()) {
            throw new NoTransactionsFoundException();
        }
        return conversionRepository.findByConversionTimeBetween(startOfDay, endOfDay, pageable);
    }

    @Override
    public Page<Conversion> getConversionHistoryByStartEndTime(LocalDate start, LocalDate end, Pageable pageable) {
        LocalDateTime startOfDay = start.atStartOfDay();
        LocalDateTime endOfDay = end.atTime(LocalTime.MAX);
        if (conversionRepository.findByConversionTimeBetween(startOfDay, endOfDay, pageable).isEmpty()) {
            throw new NoTransactionsFoundException();
        }
        return conversionRepository.findByConversionTimeBetween(startOfDay, endOfDay, pageable);
    }
}
