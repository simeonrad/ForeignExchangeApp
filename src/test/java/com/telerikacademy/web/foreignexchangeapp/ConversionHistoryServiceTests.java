package com.telerikacademy.web.foreignexchangeapp;

import com.telerikacademy.web.foreignexchangeapp.exceptions.NoTransactionsFoundException;
import com.telerikacademy.web.foreignexchangeapp.exceptions.TransactionNotFoundException;
import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import com.telerikacademy.web.foreignexchangeapp.repositories.ConversionRepository;
import com.telerikacademy.web.foreignexchangeapp.services.ConversionHistoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConversionHistoryServiceTests {
    @Mock
    private ConversionRepository conversionRepository;

    @InjectMocks
    private ConversionHistoryImpl conversionHistory;

    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        pageable = mock(Pageable.class);
    }

    @Test
    public void givenTransactionId_whenTransactionFound_thenSuccess() {
        // Arrange
        String transactionId = "123";
        Conversion conversion = mock(Conversion.class);
        given(conversionRepository.findByTransactionId(transactionId)).willReturn(Optional.of(conversion));

        // Act
        Optional<Conversion> result = conversionHistory.getConversionTransactionById(transactionId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(conversion, result.get());
    }

    @Test
    public void givenTransactionId_whenNoTransactionFound_thenThrowException() {
        // Arrange
        String transactionId = "123";
        given(conversionRepository.findByTransactionId(transactionId)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransactionNotFoundException.class, () -> conversionHistory.getConversionTransactionById(transactionId));
    }

    @Test
    public void givenDate_whenTransactionsFound_thenSuccess() {
        // Arrange
        LocalDate date = LocalDate.of(2022, 1, 1);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        Page<Conversion> expectedPage = mock(Page.class);
        when(conversionRepository.findByConversionTimeBetween(eq(startOfDay), eq(endOfDay), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Act
        Page<Conversion> resultPage = conversionHistory.getConversionsByDate(date, pageable);

        // Assert
        assertNotNull(resultPage);
        assertEquals(expectedPage, resultPage);
    }

    @Test
    public void givenDate_whenNoTransactionsFound_thenThrowException() {
        // Arrange
        LocalDate date = LocalDate.of(2022, 1, 1);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        when(conversionRepository.findByConversionTimeBetween(eq(startOfDay), eq(endOfDay), any(Pageable.class)))
                .thenReturn(Page.empty());

        // Act & Assert
        assertThrows(NoTransactionsFoundException.class, () -> conversionHistory.getConversionsByDate(date, pageable));
    }

    @Test
    public void givenStartAndEndTime_whenTransactionsFound_thenSuccess() {
        // Arrange
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 31);
        LocalDateTime startOfStartDay = startDate.atStartOfDay();
        LocalDateTime endOfEndDay = endDate.atTime(LocalTime.MAX);

        Page<Conversion> expectedPage = mock(Page.class);
        when(conversionRepository.findByConversionTimeBetween(eq(startOfStartDay), eq(endOfEndDay), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Act
        Page<Conversion> resultPage = conversionHistory.getConversionHistoryByStartEndTime(startDate, endDate, pageable);

        // Assert
        assertNotNull(resultPage);
        assertEquals(expectedPage, resultPage);
    }

    @Test
    public void givenStartAndEndTime_whenNoTransactionsFound_thenThrowException() {
        // Arrange
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 31);
        LocalDateTime startOfStartDay = startDate.atStartOfDay();
        LocalDateTime endOfEndDay = endDate.atTime(LocalTime.MAX);

        when(conversionRepository.findByConversionTimeBetween(eq(startOfStartDay), eq(endOfEndDay), any(Pageable.class)))
                .thenReturn(Page.empty());

        // Act & Assert
        assertThrows(NoTransactionsFoundException.class, () ->
                conversionHistory.getConversionHistoryByStartEndTime(startDate, endDate, pageable));
    }



}
