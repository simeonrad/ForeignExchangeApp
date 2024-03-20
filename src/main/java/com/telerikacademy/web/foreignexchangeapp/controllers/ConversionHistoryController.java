package com.telerikacademy.web.foreignexchangeapp.controllers;

import com.telerikacademy.web.foreignexchangeapp.exceptions.NoTransactionsFoundException;
import com.telerikacademy.web.foreignexchangeapp.exceptions.TransactionNotFoundException;
import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import com.telerikacademy.web.foreignexchangeapp.services.ConversionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;


@RestController
@RequestMapping("/api/conversion-history")
public class ConversionHistoryController {

    private final ConversionHistory conversionHistory;

    @Autowired
    public ConversionHistoryController(ConversionHistory conversionHistory) {
        this.conversionHistory = conversionHistory;
    }

    @GetMapping("/when-to")
    public ResponseEntity<Page<Conversion>> getConversionHistoryByStartAndEndDate(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start, @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end, @RequestParam("page") int page, @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Conversion> historyPage = conversionHistory.getConversionHistoryByStartEndTime(start, end, pageable);
            return ResponseEntity.ok(historyPage);
        } catch (NoTransactionsFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/date")
    public ResponseEntity<Page<Conversion>> getConversionHistoryForSpecificDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("page") int page, @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Conversion> conversions = conversionHistory.getConversionsByDate(date, pageable);
            return ResponseEntity.ok(conversions);
        } catch (NoTransactionsFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Conversion> getConversionById(@PathVariable String transactionId) {
        try {
            Optional<Conversion> conversion = conversionHistory.getConversionTransactionById(transactionId);
            return conversion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (TransactionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}