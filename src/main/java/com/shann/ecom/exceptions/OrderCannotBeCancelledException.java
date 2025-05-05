package com.shann.ecom.exceptions;

public class OrderCannotBeCancelledException extends Exception {
  public OrderCannotBeCancelledException(String message) {
    super(message);
  }
}
