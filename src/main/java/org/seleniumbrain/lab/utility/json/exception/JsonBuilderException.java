package org.seleniumbrain.lab.utility.json.exception;

public class JsonBuilderException extends RuntimeException {

    public JsonBuilderException() {
        super();
    }

    public JsonBuilderException(String message) {
        super(message);
    }

    public JsonBuilderException(Throwable e) {
        super(e);
    }

    public JsonBuilderException(String message, Throwable e) {
        super(message, e);
    }
}
