package com.telerikacademy.web.foreignexchangeapp;

import com.telerikacademy.web.foreignexchangeapp.exceptions.CurrencyNotFoundException;
import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import com.telerikacademy.web.foreignexchangeapp.repositories.ConversionRepository;
import com.telerikacademy.web.foreignexchangeapp.services.CurrencyConversionImpl;
import com.telerikacademy.web.foreignexchangeapp.services.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CurrencyConversionServiceTests {

    @Mock
    private ConversionRepository conversionRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private CurrencyConversionImpl currencyConversion;

    @Test
    public void whenExchangeRateAvailable_thenConvertCurrency() {
        // Arrange
        BigDecimal amount = new BigDecimal("100");
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal rate = new BigDecimal("0.85");
        Conversion expectedConversion = new Conversion();

        given(exchangeRateService.fetchCurrentExchangeRate(sourceCurrency, targetCurrency)).willReturn(rate);
        given(conversionRepository.save(any(Conversion.class))).willReturn(expectedConversion);

        // Act
        Conversion result = currencyConversion.convertCurrency(amount, sourceCurrency, targetCurrency);

        // Assert
        assertNotNull(result);
        assertEquals(expectedConversion, result);
    }

    @Test
    public void whenExchangeRateNotAvailable_thenThrowCurrencyNotFoundException() {
        // Arrange
        BigDecimal amount = new BigDecimal("100");
        String sourceCurrency = "USD";
        String targetCurrency = "MARS";

        given(exchangeRateService.fetchCurrentExchangeRate(sourceCurrency, targetCurrency)).willReturn(null);

        // Act & Assert
        assertThrows(CurrencyNotFoundException.class, () -> currencyConversion.convertCurrency(amount, sourceCurrency, targetCurrency));
    }



}