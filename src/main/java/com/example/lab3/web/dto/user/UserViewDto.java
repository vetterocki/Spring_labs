package com.example.lab3.web.dto.user;

import com.example.lab3.model.Role;
import lombok.Data;

@Data
public abstract class UserViewDto {
  private Long id;

  protected String email;

  protected Role role;
}
