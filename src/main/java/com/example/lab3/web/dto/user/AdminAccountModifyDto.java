package com.example.lab3.web.dto.user;

import com.example.lab3.model.Role;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonTypeName("admin")
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminAccountModifyDto extends UserModifyDto {
  protected Role role = Role.ADMIN;
}
