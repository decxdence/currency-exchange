package com.decxdence.currencyexchange.exception;

public class ExchangeRateAlreadyExists extends ApiException {
    public ExchangeRateAlreadyExists() {

        super("Exchange rate already exists");
    }

    public ExchangeRateAlreadyExists(Throwable cause) {
        super("Exchange rate already exists", cause);
    }

    @Override
    public int getStatus() {
        return 409;
    }
}
