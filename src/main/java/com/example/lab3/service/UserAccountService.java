package com.example.lab3.service;

import com.example.lab3.data.PostRepository;
import com.example.lab3.data.TopicRepository;
import com.example.lab3.data.UserAccountRepository;
import com.example.lab3.model.Post;
import com.example.lab3.model.Role;
import com.example.lab3.model.Topic;
import com.example.lab3.model.UserAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {
  private final UserAccountRepository userAccountRepository;
  private final PostRepository postRepository;
  private final TopicRepository topicRepository;

  @Autowired
  public UserAccountService(UserAccountRepository userAccountRepository,
                            PostRepository postRepository, TopicRepository topicRepository) {
    this.userAccountRepository = userAccountRepository;
    this.postRepository = postRepository;
    this.topicRepository = topicRepository;
  }

  public List<UserAccount> findAll() {
    return userAccountRepository.findAll();
  }

  public Optional<UserAccount> findById(Long id) {
    return userAccountRepository.findById(id);
  }

  public void deleteById(Long id) {
    userAccountRepository.deleteById(id);
  }

  public UserAccount register(UserAccount entity) {
    if (!userAccountRepository.existsByEmail(entity.getEmail())) {
      return userAccountRepository.save(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("User with email %s already exists", entity.getEmail()));
    }
  }

  public UserAccount update(Long id, UserAccount updated) {
    return findById(id)
        .map(userAccount -> {
          if (checkIfCorrectUpdate(updated)) {
            return userAccountRepository.save(userAccount);
          } else {
            throw new IllegalArgumentException(
                String.format("User with email %s already exists", updated.getEmail()));
          }
        })
        .orElseThrow(IllegalArgumentException::new);
  }

  private boolean checkIfCorrectUpdate(UserAccount updated) {
    if (!userAccountRepository.existsByEmail(updated.getEmail())) {
      return true;
    } else {
      return userAccountRepository.findByEmail(updated.getEmail())
          .filter(userAccount -> updated.getId().equals(userAccount.getId()))
          .isPresent();
    }
  }

  public Optional<UserAccount> findByEmail(String email) {
    return userAccountRepository.findByEmail(email);
  }

  public boolean isUserAdmin(UserAccount userAccount) {
    return userAccount.getRole().equals(Role.ADMIN);
  }

  public List<Post> findAllPostsByUser(UserAccount userAccount) {
    return postRepository.findAllByUserAccount(userAccount);
  }

  public List<Topic> findAllTopicsByUser(UserAccount userAccount) {
    return topicRepository.findAllByUserAccount(userAccount);
  }


}
