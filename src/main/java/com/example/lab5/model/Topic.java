package com.example.lab5.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Topic {
  private Long id;

  private String topicName;

  private LocalDateTime createdAt;

  private String description;

  private UserAccount creator;

  private boolean isArchived = false;

  private final List<Post> posts = new ArrayList<>();

  public Topic(String topicName, String description, UserAccount creator) {
    this.topicName = topicName;
    this.description = description;
    this.creator = creator;
    this.createdAt = LocalDateTime.now();
  }
}
