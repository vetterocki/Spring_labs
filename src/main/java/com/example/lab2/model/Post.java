package com.example.lab2.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


@NoArgsConstructor
@Data
public class Post {
  private Long id;

  private String postName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  private UserAccount creator;

  private String content;

  private Topic topic;

  public Post(String postName, UserAccount creator, String content, Topic topic) {
    this.postName = postName;
    this.creator = creator;
    this.content = content;
    this.topic = topic;
    this.createdAt = LocalDateTime.now();
  }
}
