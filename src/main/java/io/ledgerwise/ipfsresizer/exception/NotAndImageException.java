package io.ledgerwise.ipfsresizer.exception;

public class NotAndImageException extends RuntimeException {
    public NotAndImageException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
