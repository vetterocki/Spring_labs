package com.example.lab5.web.dto.user;

import com.example.lab5.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UserAccountModifyDto  {
  @NotBlank(message = "Specify email")
  @Email(message = "Value is not email-typed")
  protected String email;

  @NotBlank(message = "Specify password")
  protected String password;

  protected Role role = Role.USER;
}
