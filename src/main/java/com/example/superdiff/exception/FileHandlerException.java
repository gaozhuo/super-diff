package com.example.superdiff.exception;

public class FileHandlerException extends Exception {
    public FileHandlerException() {
    }

    public FileHandlerException(String message) {
        super(message);
    }

    public FileHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileHandlerException(Throwable cause) {
        super(cause);
    }
}
