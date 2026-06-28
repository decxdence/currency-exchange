package com.decxdence.currencyexchange.exception;

public class DatabaseException extends ApiException {

    public DatabaseException(Throwable cause) {
        super("Database Exception", cause);
    }

    public DatabaseException(String message) {
        super(message);
    }

    public int getStatus() {
        return 500;
    }
}
