package com.example.lab3.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserAccount {
  protected Long id;

  protected String email;

  protected String password;

  protected Role role = Role.USER;

  protected final List<Post> posts = new ArrayList<>();

  public UserAccount(String email, String password) {
    this.email = email;
    this.password = password;
  }

}
