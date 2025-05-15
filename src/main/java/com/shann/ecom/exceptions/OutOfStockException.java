package com.shann.ecom.exceptions;

public class OutOfStockException extends Exception {
    public OutOfStockException(){
        super("The product is out of stock");
    }
    public OutOfStockException(String message){
        super(message);
    }
}
