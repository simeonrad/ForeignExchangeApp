package com.telerikacademy.web.foreignexchangeapp.controllers;

import com.telerikacademy.web.foreignexchangeapp.exceptions.CurrencyNotFoundException;
import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import com.telerikacademy.web.foreignexchangeapp.models.ConversionDTO;
import com.telerikacademy.web.foreignexchangeapp.services.CurrencyConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/convert")
public class CurrencyConversionController {

    private final CurrencyConversion currencyConversion;

    @Autowired
    public CurrencyConversionController(CurrencyConversion currencyConversion) {
        this.currencyConversion = currencyConversion;
    }

    @PostMapping
    public ResponseEntity<Conversion> convertCurrency(@RequestBody ConversionDTO request) {
        try {
            Conversion result = currencyConversion.convertCurrency(request.getAmount(), request.getSourceCurrency(), request.getTargetCurrency());
            return ResponseEntity.ok(result);
        } catch (CurrencyNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
