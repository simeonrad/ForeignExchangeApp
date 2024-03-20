package com.telerikacademy.web.foreignexchangeapp.exceptions;

public class NoTransactionsFoundException extends RuntimeException {

    public static final String NO_TRANSACTIONS_ERROR_MESSAGE = "No transactions found for the period of time requested!";

    public NoTransactionsFoundException () {
        super(NO_TRANSACTIONS_ERROR_MESSAGE);
    }
}
