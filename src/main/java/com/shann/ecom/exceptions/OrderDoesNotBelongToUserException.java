package com.shann.ecom.exceptions;

public class OrderDoesNotBelongToUserException extends Exception {
  public OrderDoesNotBelongToUserException(String message) {
    super(message);
  }
}
