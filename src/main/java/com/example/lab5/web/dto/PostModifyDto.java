package com.example.lab5.web.dto;

import com.example.lab5.model.PostType;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class PostModifyDto {
  private String postName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  private Long creatorId;

  private String content;

  private Long topicId;

  private PostType postType;
}
