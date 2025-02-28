package com.emobile.springtodo.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE_FORMAT = "Account with username '%s' already exists";

    public AccountAlreadyExistsException(String username) {
        super(String.format(EXCEPTION_MESSAGE_FORMAT, username));
    }
}
