package com.example.lab5.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostViewDto extends PostModifyDto {
  private Long id;
}
