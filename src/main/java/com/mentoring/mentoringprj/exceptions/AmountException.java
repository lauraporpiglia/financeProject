package com.mentoring.mentoringprj.exceptions;

public class AmountException extends  RuntimeException {
    public AmountException(String message) {
        super(message);
    }

    public AmountException(String message, Throwable cause) {
        super(message, cause);
    }
}


