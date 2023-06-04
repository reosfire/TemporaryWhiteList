package ru.reosfire.twl.common.versioning;

public class VersionCheckException extends RuntimeException {
    public VersionCheckException() {
    }

    public VersionCheckException(String message) {
        super(message);
    }

    public VersionCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionCheckException(Throwable cause) {
        super(cause);
    }

    public VersionCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}