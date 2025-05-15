package com.shann.ecom.exceptions;

public class InvalidAddressException extends Exception {
    public InvalidAddressException(){
        super("Invalid address provided");
    }
    public InvalidAddressException(String message){
        super(message);
    }
}
