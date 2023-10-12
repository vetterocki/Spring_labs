package com.example.lab2.data;

import com.example.lab2.model.UserAccount;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserAccountRepository extends StubCrudRepository<Long, UserAccount> {
  @Override
  public List<UserAccount> findAll() {
    return super.entities;
  }

  @Override
  public Optional<UserAccount> findById(Long id) {
    return super.entities.stream()
        .filter(user -> user.getId().equals(id))
        .findFirst();
  }

  @Override
  public void deleteById(Long id) {
    super.entities.stream()
        .filter(userAccount -> userAccount.getId().equals(id))
        .findFirst()
        .ifPresent(super.entities::remove);
  }

  @Override
  public UserAccount save(UserAccount entity) {
    return super.entities.stream()
        .filter(userAccount -> Objects.nonNull(userAccount.getId()))
        .filter(userAccount -> userAccount.getId().equals(entity.getId()))
        .findFirst()
        .map(userAccount -> {
          super.entities.set(super.entities.indexOf(userAccount), entity);
          return entity;
        })
        .orElseGet(() -> {
          entity.setId(identityCounter++);
          super.entities.add(entity);
          return entity;
        });


  }

  public Optional<UserAccount> findByEmail(String email) {
    return super.entities.stream()
        .filter(userAccount -> userAccount.getEmail().equals(email))
        .findFirst();
  }

  @Override
  public List<UserAccount> saveAll(Collection<UserAccount> entities) {
    return entities.stream()
        .map(this::save)
        .toList();
  }

  public boolean existsByEmail(String email) {
    return super.entities.stream()
        .anyMatch(userAccount -> userAccount.getEmail().equals(email));
  }

}
