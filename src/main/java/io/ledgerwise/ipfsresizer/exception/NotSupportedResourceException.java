package io.ledgerwise.ipfsresizer.exception;

public class NotSupportedResourceException extends RuntimeException {
   public NotSupportedResourceException(String errorMessage, Throwable err) {
      super(errorMessage, err);
   }
}
