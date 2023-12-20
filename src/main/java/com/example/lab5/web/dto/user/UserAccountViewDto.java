package com.example.lab5.web.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAccountViewDto extends UserAccountModifyDto {
  private Long id;
}
