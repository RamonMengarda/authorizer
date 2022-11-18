package com.ramonmengarda.authorizer.exceptions;

public class NonexistentCardException extends Exception {
    public NonexistentCardException(String errorMessage) {
        super(errorMessage);
    }
}
