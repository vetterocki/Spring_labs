package com.example.lab5.service;

import com.example.lab5.data.PostRepository;
import com.example.lab5.data.TopicRepository;
import com.example.lab5.data.UserAccountRepository;
import com.example.lab5.exception.UserAlreadyExistsException;
import com.example.lab5.model.Post;
import com.example.lab5.model.Topic;
import com.example.lab5.model.UserAccount;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Service
public class UserAccountService {
  private final UserAccountRepository userAccountRepository;
  private final UserAccountService self;
  private final TopicRepository topicRepository;
  private final PostRepository postRepository;

  public List<Topic> findAllTopics(UserAccount userAccount) {
    return topicRepository.findAllByCreator(userAccount);
  }

  public List<Post> findAllByPosts(UserAccount userAccount) {
    return postRepository.findAllByCreator(userAccount);
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

  @Transactional
  public UserAccount register(UserAccount entity) {
    if (!userAccountRepository.existsByEmail(entity.getEmail())) {
      return userAccountRepository.save(entity);
    } else {
      throw new UserAlreadyExistsException(
          String.format("User with email %s already exists", entity.getEmail()));
    }
  }

  @Transactional
  public UserAccount update(Long id, UserAccount updated) {
    updated.setId(id);
    if (!userAccountRepository.existsByEmail(updated.getEmail()))
      return self.register(updated);
    else {
      throw new UserAlreadyExistsException(
          String.format("User with email %s already exists", updated.getEmail()));
    }
  }

  public Optional<UserAccount> findByEmail(String email) {
    return userAccountRepository.findByEmail(email);
  }


}
