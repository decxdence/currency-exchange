package com.decxdence.currencyexchange.exception;

public class InvalidPathException extends ApiException {

    public InvalidPathException(Throwable cause) {
        super("Invalid path", cause);
    }

    public InvalidPathException() {
        super("Invalid path");
    }

    public int getStatus(){
        return 400;
    }
}
