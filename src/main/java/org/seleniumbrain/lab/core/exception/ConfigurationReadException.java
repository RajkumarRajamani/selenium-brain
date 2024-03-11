package org.seleniumbrain.lab.core.exception;

public class ConfigurationReadException extends SeleniumBrainException {
    public ConfigurationReadException() {
        super();
    }

    public ConfigurationReadException(String errorMessage) {
        super(errorMessage);
    }

    public ConfigurationReadException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}
