package com.example.lab5.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class Post {
  private Long id;

  private String postName;

  private LocalDateTime createdAt;

  private UserAccount creator;

  private String content;

  private Topic topic;

  private PostType postType;

  public Post(String postName, UserAccount creator, String content, Topic topic, PostType postType) {
    this.postName = postName;
    this.creator = creator;
    this.content = content;
    this.topic = topic;
    this.createdAt = LocalDateTime.now();
    this.postType = postType;
  }

}
