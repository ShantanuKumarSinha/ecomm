package com.shann.ecom.exceptions;

public class InvalidProductException extends Exception {
    public InvalidProductException() {
        super("Invalid product provided");
    }

    public InvalidProductException(String message) {
        super(message);
    }
}
