package com.comviva.api.utils;

public class FunctionException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private static final String public_MESSAGE = "Macros Exception";

    public FunctionException() {
        super(public_MESSAGE);
    }

    public FunctionException(Exception e) {
        super(e);
    }

    public FunctionException(String msg) {
        super(msg);
    }

    public FunctionException(String msg, Throwable e) {
        super(msg, e);
    }

    public String toString() {
        return ("Macros Exception:" + this.getMessage());
    }

}
