package com.example.lab3.data;

import com.example.lab3.model.Post;
import com.example.lab3.model.Topic;
import com.example.lab3.model.UserAccount;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepository extends StubCrudRepository<Long, Post> {
  @Override
  public List<Post> findAll() {
    return super.entities;
  }

  @Override
  public Optional<Post> findById(Long id) {
    return super.entities.stream()
        .filter(user -> user.getId().equals(id))
        .findFirst();
  }

  @Override
  public void deleteById(Long id) {
    super.entities.stream()
        .filter(post -> post.getId().equals(id))
        .findFirst()
        .ifPresent(super.entities::remove);
  }

  @Override
  public Post save(Post entity) {
    return super.entities.stream()
        .filter(post -> Objects.nonNull(post.getId()))
        .filter(post -> post.getId().equals(entity.getId()))
        .findFirst()
        .map(post -> {
          super.entities.set(super.entities.indexOf(post), entity);
          return entity;
        })
        .orElseGet(() -> {
          entity.setId(identityCounter++);
          super.entities.add(entity);
          return entity;
        });
  }

  @Override
  public List<Post> saveAll(Collection<Post> entities) {
    return entities.stream()
        .map(this::save)
        .toList();
  }

  public List<Post> findAllByTopic(Topic topic) {
    return super.entities.stream()
        .filter(post -> post.getTopic().equals(topic))
        .toList();
  }

  public List<Post> findAllByUserAccount(UserAccount userAccount) {
    return super.entities.stream()
        .filter(post -> post.getCreator().equals(userAccount))
        .toList();
  }
}
