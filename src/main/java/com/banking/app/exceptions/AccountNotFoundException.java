package com.banking.app.exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String accountNotFound) {
        super(accountNotFound);
    }
}
