package com.example.lab2.data;

import com.example.lab2.model.Topic;
import com.example.lab2.model.UserAccount;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TopicRepository extends StubCrudRepository<Long, Topic> {
  @Override
  public List<Topic> findAll() {
    return super.entities;
  }

  @Override
  public Optional<Topic> findById(Long id) {
    return super.entities.stream()
        .filter(topic -> topic.getId().equals(id))
        .findFirst();
  }

  @Override
  public void deleteById(Long id) {
    super.entities.stream()
        .filter(topic -> topic.getId().equals(id))
        .findFirst()
        .ifPresent(super.entities::remove);
  }

  @Override
  public Topic save(Topic entity) {
    return super.entities.stream()
        .filter(topic -> Objects.nonNull(topic.getId()))
        .filter(topic -> topic.getId().equals(entity.getId()))
        .findFirst()
        .map(topic-> {
          super.entities.set(super.entities.indexOf(topic), entity);
          return entity;
        })
        .orElseGet(() -> {
          entity.setId(identityCounter++);
          super.entities.add(entity);
          return entity;
        });
  }

  @Override
  public List<Topic> saveAll(Collection<Topic> entities) {
    return entities.stream()
        .map(this::save)
        .toList();
  }

  public List<Topic> findAllByUserAccount(UserAccount userAccount) {
    return super.entities.stream()
        .filter(topic -> topic.getCreator().equals(userAccount))
        .toList();
  }
}
