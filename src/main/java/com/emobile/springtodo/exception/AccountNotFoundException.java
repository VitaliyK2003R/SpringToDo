package com.emobile.springtodo.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE_FORMAT = "Account with id '%s' is not found";

    public AccountNotFoundException(UUID accountId) {
        super(String.format(EXCEPTION_MESSAGE_FORMAT, accountId));
    }
}
