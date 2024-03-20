package com.telerikacademy.web.foreignexchangeapp.models;

import java.math.BigDecimal;

public class ConversionDTO {
    private BigDecimal amount;
    private String sourceCurrency;
    private String targetCurrency;
    public ConversionDTO(BigDecimal amount, String sourceCurrency, String targetCurrency) {
        this.amount = amount;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }
}

