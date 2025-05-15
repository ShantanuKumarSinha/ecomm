package com.shann.ecom.exceptions;

public class HighDemandProductException extends Exception {
    public HighDemandProductException() {
        super("The product is in high demand");
    }

    public HighDemandProductException(String message) {
        super(message);
    }
}
