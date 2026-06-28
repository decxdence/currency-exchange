package com.decxdence.currencyexchange.exception;

public class InvalidRequestException extends ApiException {

    public InvalidRequestException() {
        super("Invalid Request");
    }

    public InvalidRequestException(Throwable cause) {
        super("Invalid request", cause);
    }

    @Override
    public int getStatus() {
        return 400;
    }
}
