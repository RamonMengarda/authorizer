package com.ramonmengarda.authorizer.exceptions;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String errorMessage) {
        super(errorMessage);
    }
}
