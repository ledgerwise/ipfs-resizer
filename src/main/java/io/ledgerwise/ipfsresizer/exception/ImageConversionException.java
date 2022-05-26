package io.ledgerwise.ipfsresizer.exception;

public class ImageConversionException extends RuntimeException {
   public ImageConversionException(String errorMessage, Throwable err) {
      super(errorMessage, err);
   }
}
