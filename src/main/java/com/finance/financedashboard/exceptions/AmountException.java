package com.finance.financedashboard.exceptions;

public class AmountException extends  RuntimeException {
    public AmountException(String message) {
        super(message);
    }

    public AmountException(String message, Throwable cause) {
        super(message, cause);
    }
}


