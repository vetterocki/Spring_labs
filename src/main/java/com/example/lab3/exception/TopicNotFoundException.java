package com.example.lab3.exception;

public class TopicNotFoundException extends RuntimeException {
  public TopicNotFoundException(Long topicId) {
    super(String.format("Topic with id %d was not found.", topicId));
  }
}
