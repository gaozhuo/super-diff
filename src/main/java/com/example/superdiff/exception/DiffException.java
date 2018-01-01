package com.example.superdiff.exception;

public class DiffException extends Exception {
    public DiffException() {
    }

    public DiffException(String message) {
        super(message);
    }

    public DiffException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiffException(Throwable cause) {
        super(cause);
    }
}
