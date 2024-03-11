package org.seleniumbrain.lab.core.exception;

public class ApiException extends SeleniumBrainException {
    public ApiException() {
        super();
    }

    public ApiException(String errorMessage) {
        super(errorMessage);
    }

    public ApiException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}
