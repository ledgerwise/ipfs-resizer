package io.ledgerwise.ipfsresizer.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
