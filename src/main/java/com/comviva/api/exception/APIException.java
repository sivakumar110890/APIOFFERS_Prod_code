package com.comviva.api.exception;

public class APIException extends Exception {
    private static final long serialVersionUID = 1L;
    private static final String public_MESSAGE = "APIOffers Exception";

    public APIException() {
        super(public_MESSAGE);
    }

    public APIException(Exception e) {
        super(e);
    }

    public APIException(String msg) {
        super(msg);
    }

    public APIException(String msg, Throwable e) {
        super(msg, e);
    }
}
