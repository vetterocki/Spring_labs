package com.example.lab3.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TopicViewDto extends TopicModifyDto {
  private Long id;
}
