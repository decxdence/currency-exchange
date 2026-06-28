package com.decxdence.currencyexchange.exception;

import java.sql.SQLException;

public class CurrencyNotFoundException extends ApiException {
    public CurrencyNotFoundException() {
        super("Currency not found");
    }

    public CurrencyNotFoundException(Throwable cause) {
        super("Currency not found", cause);
    }

    public int getStatus() {
        return 404;
    }
}
