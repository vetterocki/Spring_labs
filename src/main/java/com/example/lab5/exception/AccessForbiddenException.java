package com.example.lab5.exception;

public class AccessForbiddenException extends RuntimeException {
  public AccessForbiddenException(String message) {
    super(message);
  }
}
