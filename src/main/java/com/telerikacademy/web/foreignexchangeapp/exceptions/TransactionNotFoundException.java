package com.telerikacademy.web.foreignexchangeapp.exceptions;

public class TransactionNotFoundException extends RuntimeException {

    public static final String NO_TRANSACTIONS_FOUND_ERROR = "Transaction with the provided id {%s} is not found!";

    public TransactionNotFoundException(String transactionId) {
        super(String.format(NO_TRANSACTIONS_FOUND_ERROR, transactionId));
    }
}
