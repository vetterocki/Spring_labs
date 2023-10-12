package com.example.lab2.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminAccount extends UserAccount {
  private final List<Topic> topics = new ArrayList<>();

  public AdminAccount(String email, String password) {
    super(email, password);
    this.role = Role.ADMIN;
  }


}
