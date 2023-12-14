package com.example.lab3.exception;

public class UserNotFoundException extends RuntimeException{
  public UserNotFoundException(String userEmail) {
    super(String.format("User with email %s was not found.", userEmail));
  }
}
