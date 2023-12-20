package com.example.lab3.web.dto.user;

import com.example.lab3.model.Role;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "jsonType", visible = true)
@JsonSubTypes(value = {
    @JsonSubTypes.Type(name = "admin", value = AdminAccountModifyDto.class),
    @JsonSubTypes.Type(name = "user", value = UserAccountModifyDto.class)
})
@Data
public abstract class UserModifyDto {

  @NotBlank(message = "Specify email")
  @Email(message = "Value is not email-typed")
  protected String email;

  @NotBlank(message = "Specify password")
  protected String password;

  protected Role role = Role.USER;

  @NotBlank(message = "Specify jsonType to get particular user role")
  private String jsonType;
}
