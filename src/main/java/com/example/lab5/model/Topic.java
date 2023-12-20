package com.example.lab5.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "topics")
public class Topic {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String topicName;

  private LocalDateTime createdAt;

  private String description;

  @ManyToOne
  @JoinColumn(name = "user_account_id")
  private UserAccount creator;

  private boolean isArchived = false;

  @OneToMany(mappedBy = "topic", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
  @ToString.Exclude
  private final List<Post> posts = new ArrayList<>();

  public Topic(String topicName, String description, UserAccount creator) {
    this.topicName = topicName;
    this.description = description;
    this.creator = creator;
    this.createdAt = LocalDateTime.now();
  }
}
