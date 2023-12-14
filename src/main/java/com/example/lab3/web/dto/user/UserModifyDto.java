package com.example.lab3.web.dto.user;

import com.example.lab3.model.Role;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "jsonType", visible = true)
@JsonSubTypes(value = {
    @JsonSubTypes.Type(name = "admin", value = AdminAccountModifyDto.class)
})
@Data
public abstract class UserModifyDto {

  protected String email;

  protected String password;

  protected Role role = Role.USER;

  private String jsonType;
}
