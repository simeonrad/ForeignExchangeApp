package com.telerikacademy.web.foreignexchangeapp.repositories;

import com.telerikacademy.web.foreignexchangeapp.models.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long> {

    Optional<Conversion> findByTransactionId(String transactionId);

    List<Conversion> findByConversionTimeBetween(LocalDateTime start, LocalDateTime end);

    Page<Conversion> findByConversionTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
