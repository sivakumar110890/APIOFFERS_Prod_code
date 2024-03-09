package com.comviva.api.exception;

/**
 * @author Nitin.Gupta
 */
public class ConfigException extends Exception {
    private static final long serialVersionUID = 1L;
    private static final String public_MESSAGE = "Configuration Exception";

    public ConfigException() {
        super(public_MESSAGE);
    }

    public ConfigException(Exception e) {
        super(e);
    }

    public ConfigException(String msg) {
        super(msg);
    }

    public ConfigException(String msg, Throwable e) {
        super(msg, e);
    }

}
