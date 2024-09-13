package com.finance.financedashboard.exceptions;

public class TransactionReadException extends Exception{

    public TransactionReadException(String message, Throwable cause) {
        super(message, cause);
    }
    public TransactionReadException(String message) {
        super(message);
    }
}
