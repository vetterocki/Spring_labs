package com.example.lab3.exception;

public class AccessForbiddenException extends RuntimeException {
  public AccessForbiddenException(String message) {
    super(message);
  }
}
