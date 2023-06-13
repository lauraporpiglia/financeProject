package com.mentoring.mentoringprj.exceptions;

public class TransactionReadException extends Exception{

    public TransactionReadException(String message, Throwable cause) {
        super(message, cause);
    }
    public TransactionReadException(String message) {
        super(message);
    }
}
