package com.example.lab3.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@Data
public class Topic {
  private Long id;

  private String topicName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  private String description;

  private AdminAccount creator;

  private boolean isArchived = false;

  private final List<Post> posts = new ArrayList<>();

  public Topic(String topicName, String description, AdminAccount creator) {
    this.topicName = topicName;
    this.description = description;
    this.creator = creator;
    this.createdAt = LocalDateTime.now();
  }
}
