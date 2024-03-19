package com.telerikacademy.web.foreignexchangeapp.services;

import com.telerikacademy.web.foreignexchangeapp.models.ExchangeRate;
import com.telerikacademy.web.foreignexchangeapp.models.ExchangeRateResponse;
import com.telerikacademy.web.foreignexchangeapp.repositories.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final RestTemplate restTemplate;
    private final ExchangeRateRepository exchangeRateRepository;


    @Value("${openex.api.key}")
    private String apiKey;

    @Value("${openex.url}")
    private String apiUrl;

    public ExchangeRateServiceImpl(RestTemplate restTemplate, ExchangeRateRepository exchangeRateRepository) {
        this.restTemplate = restTemplate;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public BigDecimal fetchCurrentExchangeRate(String sourceCurrency, String targetCurrency) {
        String url = apiUrl + "/latest.json?app_id=" + apiKey + "&symbols=" + targetCurrency;
        ResponseEntity<ExchangeRateResponse> response = restTemplate.getForEntity(url, ExchangeRateResponse.class);
        BigDecimal rate = response.getBody().getRates().get(targetCurrency);
        return rate;
    }

    @Override
    @Transactional
    public void updateExchangeRates() {
        String url = String.format("%s/latest.json?app_id=%s", apiUrl, apiKey);
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);

        if (response != null && response.getRates() != null) {
            for (Map.Entry<String, BigDecimal> entry : response.getRates().entrySet()) {
                String currency = entry.getKey();
                BigDecimal rate = entry.getValue();
                updateOrInsertExchangeRate("USD", currency, rate); //USD, because it is the only included in the free plan
            }
        }
    }

    private void updateOrInsertExchangeRate(String sourceCurrency, String targetCurrency, BigDecimal rate) {
        Optional<ExchangeRate> existingRate = exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(sourceCurrency, targetCurrency);
        if (existingRate.isPresent()) {
            ExchangeRate rateToUpdate = existingRate.get();
            rateToUpdate.setRate(rate);
            exchangeRateRepository.save(rateToUpdate);
        } else {
            ExchangeRate newRate = new ExchangeRate(sourceCurrency, targetCurrency, rate);
            exchangeRateRepository.save(newRate);
        }
    }

    @Override
    public List<ExchangeRate> findExchangeRatesBetween(LocalDateTime start, LocalDateTime end) {
        return exchangeRateRepository.findByTimestampBetween(start, end);
    }

}
