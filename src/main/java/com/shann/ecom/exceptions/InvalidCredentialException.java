package com.shann.ecom.exceptions;

public class InvalidCredentialException extends Exception {

  public InvalidCredentialException() {
    super("Invalid credentials provided");
  }
}
