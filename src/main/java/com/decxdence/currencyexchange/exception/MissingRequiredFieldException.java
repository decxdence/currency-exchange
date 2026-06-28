package com.decxdence.currencyexchange.exception;

public class MissingRequiredFieldException extends ApiException {

    public MissingRequiredFieldException() {
           super("Required field missing");
    }

    public MissingRequiredFieldException(String message, Throwable cause) {
        super("Required field missing", cause);
    }

    @Override
    public int getStatus() {
        return 400;
    }
}
