package com.banking.app.exceptions;

public class AccountBalanceNotEnoughException extends Exception {
    public AccountBalanceNotEnoughException(String balanceNotSufficient) {
        super(balanceNotSufficient);
    }
}
