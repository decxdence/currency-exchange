package com.decxdence.currencyexchange.exception;

public class ExchangeRateNotFoundException extends ApiException {

    public ExchangeRateNotFoundException() {
        super("Exchange rate not found");
    }

    public ExchangeRateNotFoundException(Throwable cause) {
        super("Exchange rate not found", cause);
    }

    @Override
    public int getStatus() {
        return 404;
    }
}
