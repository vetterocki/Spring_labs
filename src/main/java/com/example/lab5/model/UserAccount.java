package com.example.lab5.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserAccount {
  private Long id;

  private String email;

  private String password;

  private Role role;

  private final List<Post> posts = new ArrayList<>();

  private final List<Topic> topics = new ArrayList<>();

  public UserAccount(String email, String password, Role role) {
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public void addPost(Post post) {
    post.setCreator(this);
    this.posts.add(post);
  }

  public void addAllPosts(Collection<Post> posts) {
    posts.forEach(this::addPost);
  }

  public void addTopic(Topic topic) {
    topic.setCreator(this);
    this.topics.add(topic);
  }

  public void addAllTopics(Collection<Topic> topics) {
    topics.forEach(this::addTopic);
  }

  public boolean isAdmin() {
    return this.role.equals(Role.ADMIN);
  }

}
