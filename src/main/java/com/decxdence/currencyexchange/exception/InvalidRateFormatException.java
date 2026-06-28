package com.decxdence.currencyexchange.exception;

public class InvalidRateFormatException extends ApiException {

    public InvalidRateFormatException() {
        super("Invalid data format");
    }

    public InvalidRateFormatException(Throwable cause) {
        super("Invalid data format", cause);
    }

    @Override
    public int getStatus() {
        return 400;
    }
}
