package com.book.exception;

public class PasswordCompromisedException extends RuntimeException {
    public PasswordCompromisedException(String message) {
        super(message);
    }
}