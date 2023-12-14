package com.example.lab3.web.dto;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class TopicModifyDto {
  private String topicName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  private String description;

  private Long creatorId;
}
