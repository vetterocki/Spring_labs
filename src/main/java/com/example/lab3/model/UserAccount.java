package com.example.lab3.model;

import java.util.ArrayList;
import java.util.Collection;
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

  public void addPost(Post post) {
    post.setCreator(this);
    this.posts.add(post);
  }

  public void addAllPosts(Collection<Post> posts) {
    posts.forEach(this::addPost);
  }

}
