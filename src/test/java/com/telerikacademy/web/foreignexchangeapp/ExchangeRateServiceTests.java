package com.telerikacademy.web.foreignexchangeapp;

import com.telerikacademy.web.foreignexchangeapp.exceptions.CurrencyNotFoundException;
import com.telerikacademy.web.foreignexchangeapp.models.ExchangeRate;
import com.telerikacademy.web.foreignexchangeapp.models.ExchangeRateResponse;
import com.telerikacademy.web.foreignexchangeapp.repositories.ExchangeRateRepository;
import com.telerikacademy.web.foreignexchangeapp.services.ExchangeRateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTests {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private ExchangeRateServiceImpl service;

    @Test
    public void fetchCurrentExchangeRate_Success() {
        // Arrange
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal expectedRate = new BigDecimal("0.85");
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setRates(Map.of(targetCurrency, expectedRate));
        when(restTemplate.getForEntity(anyString(), eq(ExchangeRateResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        BigDecimal actualRate = service.fetchCurrentExchangeRate(sourceCurrency, targetCurrency);

        // Assert
        assertEquals(expectedRate, actualRate);
    }

    @Test
    public void fetchCurrentExchangeRate_CurrencyNotFound() {
        // Arrange
        String sourceCurrency = "USD";
        String targetCurrency = "ABC";
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setRates(Map.of());
        when(restTemplate.getForEntity(anyString(), eq(ExchangeRateResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Act & Assert
        assertThrows(CurrencyNotFoundException.class, () -> service.fetchCurrentExchangeRate(sourceCurrency, targetCurrency));
    }

    @Test
    public void updateExchangeRates_Success() {
        // Arrange
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setRates(Map.of("EUR", new BigDecimal("0.85"), "GBP", new BigDecimal("0.75")));
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(mockResponse);

        when(exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(anyString(), anyString()))
                .thenReturn(Optional.empty());

        // Act
        service.updateExchangeRates();

        // Assert
        verify(exchangeRateRepository, times(2)).save(any(ExchangeRate.class));
    }

    @Test
    public void updateExchangeRates_ApiReturnsNull() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(null);

        // Act
        service.updateExchangeRates();

        // Assert
        verifyNoInteractions(exchangeRateRepository);
    }

    @Test
    public void updateOrInsertExchangeRate_ExistingRatePresent() {
        // Arrange
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal newRate = new BigDecimal("0.85");
        ExchangeRate existingRate = new ExchangeRate(sourceCurrency, targetCurrency, BigDecimal.ZERO);

        when(exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(sourceCurrency, targetCurrency))
                .thenReturn(Optional.of(existingRate));

        // Act
        service.updateOrInsertExchangeRate(sourceCurrency, targetCurrency, newRate);

        // Assert
        verify(exchangeRateRepository).save(existingRate);
        assertEquals(newRate, existingRate.getRate());
    }

    @Test
    public void findExchangeRatesBetween_Success() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        ExchangeRate rate1 = new ExchangeRate("USD", "EUR", new BigDecimal("0.85"));
        ExchangeRate rate2 = new ExchangeRate("USD", "GBP", new BigDecimal("0.75"));
        List<ExchangeRate> expectedRates = Arrays.asList(rate1, rate2);

        when(exchangeRateRepository.findByTimestampBetween(start, end)).thenReturn(expectedRates);

        // Act
        List<ExchangeRate> actualRates = service.findExchangeRatesBetween(start, end);

        // Assert
        assertEquals(expectedRates, actualRates);
        verify(exchangeRateRepository).findByTimestampBetween(start, end);
    }

    @Test
    public void findExchangeRatesBetween_NoRatesFound() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        when(exchangeRateRepository.findByTimestampBetween(start, end)).thenReturn(Collections.emptyList());

        // Act
        List<ExchangeRate> actualRates = service.findExchangeRatesBetween(start, end);

        // Assert
        assertEquals(Collections.emptyList(), actualRates);
        verify(exchangeRateRepository).findByTimestampBetween(start, end);
    }

}
