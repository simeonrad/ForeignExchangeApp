package com.telerikacademy.web.foreignexchangeapp.controllers;

import com.telerikacademy.web.foreignexchangeapp.exceptions.CurrencyNotFoundException;
import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import com.telerikacademy.web.foreignexchangeapp.models.ConversionDTO;
import com.telerikacademy.web.foreignexchangeapp.services.CurrencyConversion;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/convert")
public class CurrencyConversionController {

    private final CurrencyConversion currencyConversion;

    @Autowired
    public CurrencyConversionController(CurrencyConversion currencyConversion) {
        this.currencyConversion = currencyConversion;
    }

    @PostMapping
    @Operation(
            summary = "Convert Currency",
            description = "Converts a specified amount from a source currency to a target currency. This operation requires specifying the amount to convert, the source currency code, and the target currency code.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload containing the amount to convert, source currency, and target currency.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ConversionDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful currency conversion. Returns the converted amount along with the source and target currencies.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Conversion.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Currency not found. Occurs when the specified source or target currency does not exist."
                    )
            }
    )
    public ResponseEntity<Conversion> convertCurrency(@RequestBody ConversionDTO request) {
        try {
            Conversion result = currencyConversion.convertCurrency(request.getAmount(), request.getSourceCurrency(), request.getTargetCurrency());
            return ResponseEntity.ok(result);
        } catch (CurrencyNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
