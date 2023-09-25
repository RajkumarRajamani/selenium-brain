package org.seleniumbrain.lab.exception;

public class SeleniumBrainException extends RuntimeException {
    public SeleniumBrainException() {
        super();
    }

    public SeleniumBrainException(String errorMessage) {
        super(errorMessage);
    }

    public SeleniumBrainException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}
