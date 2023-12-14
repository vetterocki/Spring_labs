package com.example.lab3.web.dto.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonTypeName("user")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAccountModifyDto extends UserModifyDto {
}
