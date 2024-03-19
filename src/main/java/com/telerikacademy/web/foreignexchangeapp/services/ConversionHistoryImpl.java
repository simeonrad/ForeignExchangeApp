package com.telerikacademy.web.foreignexchangeapp.services;

import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import com.telerikacademy.web.foreignexchangeapp.repositories.ConversionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConversionHistoryImpl implements ConversionHistory {
    private final ConversionRepository conversionRepository;

    public ConversionHistoryImpl(ConversionRepository conversionRepository) {
        this.conversionRepository = conversionRepository;
    }

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
