package de.arthurpicht.fzfJavaWrapper;

public class FzfRuntimeException extends RuntimeException {

    public FzfRuntimeException() {
    }

    public FzfRuntimeException(String message) {
        super(message);
    }

    public FzfRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FzfRuntimeException(Throwable cause) {
        super(cause);
    }

    public FzfRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
