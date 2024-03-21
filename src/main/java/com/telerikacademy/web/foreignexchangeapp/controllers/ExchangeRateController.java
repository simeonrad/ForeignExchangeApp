package com.telerikacademy.web.foreignexchangeapp.controllers;

import com.telerikacademy.web.foreignexchangeapp.exceptions.CurrencyNotFoundException;
import com.telerikacademy.web.foreignexchangeapp.models.ExchangeRateDTO;
import com.telerikacademy.web.foreignexchangeapp.services.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Operation(
            summary = "Fetch Current Exchange Rate",
            description = "Retrieves the current exchange rate between two specified currencies. This operation requires the source and target currency codes as path variables.",
            parameters = {
                    @Parameter(
                            name = "sourceCurrency",
                            description = "The currency code of the source currency from which to convert.",
                            required = true,
                            example = "USD"
                    ),
                    @Parameter(
                            name = "targetCurrency",
                            description = "The currency code of the target currency to which to convert.",
                            required = true,
                            example = "EUR"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the current exchange rate.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExchangeRateDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Currency not found. This response occurs when one or both of the specified currencies are not recognized."
                    )
            }
    )
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