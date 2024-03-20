package com.telerikacademy.web.foreignexchangeapp.exceptions;

public class CurrencyNotFoundException extends RuntimeException{
    public CurrencyNotFoundException(String targetCurrency) {
        super(String.format
                ("The target currency code {%s} you provided is not valid!", targetCurrency));
    }
}
