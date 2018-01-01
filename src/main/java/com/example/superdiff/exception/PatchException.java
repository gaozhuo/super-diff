package com.example.superdiff.exception;

public class PatchException extends Exception {
    public PatchException() {
    }

    public PatchException(String message) {
        super(message);
    }

    public PatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PatchException(Throwable cause) {
        super(cause);
    }
}
