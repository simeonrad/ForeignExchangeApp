package com.telerikacademy.web.foreignexchangeapp.repositories;

import com.telerikacademy.web.foreignexchangeapp.models.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findBySourceCurrencyAndTargetCurrency(String sourceCurrency, String targetCurrency);
    List<ExchangeRate> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}