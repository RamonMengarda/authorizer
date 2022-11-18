package com.ramonmengarda.authorizer.exceptions;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String errorMessage) {
        super(errorMessage);
    }
}
