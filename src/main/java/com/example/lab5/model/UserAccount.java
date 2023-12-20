package com.example.lab5.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@NoArgsConstructor
@Getter
@Setter
@ToString
@NamedQuery(name = "findByEmail", query = "SELECT u FROM UserAccount u WHERE u.email = :email")
@Entity
@Table(name = "user_accounts")
public class UserAccount {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "creator", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
  @ToString.Exclude
  private final List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "creator", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
  @ToString.Exclude
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

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy ?
        ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() :
        o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
        ((HibernateProxy) this).getHibernateLazyInitializer()
            .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    UserAccount that = (UserAccount) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ?
        ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
        getClass().hashCode();
  }
}
