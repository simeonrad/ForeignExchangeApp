package com.telerikacademy.web.foreignexchangeapp.controllers;

import com.telerikacademy.web.foreignexchangeapp.exceptions.NoTransactionsFoundException;
import com.telerikacademy.web.foreignexchangeapp.exceptions.TransactionNotFoundException;
import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import com.telerikacademy.web.foreignexchangeapp.services.ConversionHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Operation(
            summary = "Get Conversion History",
            description = "Retrieves the history of conversions within a specified start and end date range. The history is paginated, allowing clients to specify the page number and the number of records per page.",
            parameters = {
                    @Parameter(
                            name = "start",
                            description = "The start date from which the search should begin, in ISO date-time format (e.g., YYYY-MM-DDTHH:MM:SS).",
                            required = true,
                            example = "2023-01-01T00:00:00"
                    ),
                    @Parameter(
                            name = "end",
                            description = "The end date at which the search should end, in ISO date-time format (e.g., YYYY-MM-DDTHH:MM:SS).",
                            required = true,
                            example = "2023-01-31T23:59:59"
                    ),
                    @Parameter(
                            name = "page",
                            description = "The page number to retrieve, starting from 0.",
                            required = true,
                            example = "0"
                    ),
                    @Parameter(
                            name = "size",
                            description = "The number of records per page.",
                            required = true,
                            example = "10"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the conversion history.",
                            content = @Content(
                                    schema = @Schema(implementation = Conversion.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No conversion history found for the provided date range or parameters."
                    )
            }
    )
    public ResponseEntity<Page<Conversion>> getConversionHistoryByStartAndEndDate(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Conversion> historyPage = conversionHistory.getConversionHistoryByStartEndTime(start, end, pageable);
            return ResponseEntity.ok(historyPage);
        } catch (NoTransactionsFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/date")
    @Operation(
            summary = "Get Conversion History for Specific Date",
            description = "Retrieves a paginated list of conversions that occurred on a specific date. Allows specifying the page number and the number of records per page to manage data volume.",
            parameters = {
                    @Parameter(
                            name = "date",
                            description = "The specific date for which to retrieve conversion history, in ISO date format (YYYY-MM-DD).",
                            required = true,
                            example = "2023-01-01"
                    ),
                    @Parameter(
                            name = "page",
                            description = "The page number of the conversion history to retrieve, starting from 0.",
                            required = true,
                            example = "0"
                    ),
                    @Parameter(
                            name = "size",
                            description = "The number of records per page in the conversion history.",
                            required = true,
                            example = "10"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved conversion history for the specified date.",
                            content = @Content(
                                    schema = @Schema(implementation = Conversion.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No conversion history found for the specified date."
                    )
            }
    )
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
    @Operation(
            summary = "Get Conversion by Transaction ID",
            description = "Retrieves a single conversion transaction using its unique transaction ID. This endpoint facilitates fetching detailed information about a specific conversion transaction.",
            parameters = {
                    @Parameter(
                            name = "transactionId",
                            description = "The unique identifier of the conversion transaction to be retrieved.",
                            required = true,
                            example = "abc123"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the conversion transaction.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Conversion.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No conversion transaction found for the given transaction ID."
                    )
            }
    )
    public ResponseEntity<Conversion> getConversionById(@PathVariable String transactionId) {
        try {
            Optional<Conversion> conversion = conversionHistory.getConversionTransactionById(transactionId);
            return conversion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (TransactionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}