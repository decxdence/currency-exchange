package com.decxdence.currencyexchange.exception;

public class CurrencyAlreadyExistsException extends ApiException {

    public CurrencyAlreadyExistsException() {
        super("Currency already exists");
    }

    public CurrencyAlreadyExistsException(Throwable cause) {
        super("Currency already exists", cause);
    }

    @Override
    public int getStatus() {
        return 409;
    }
}
