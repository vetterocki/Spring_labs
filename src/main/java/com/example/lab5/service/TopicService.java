package com.example.lab5.service;

import com.example.lab5.data.PostRepository;
import com.example.lab5.data.TopicRepository;
import com.example.lab5.exception.AccessForbiddenException;
import com.example.lab5.model.Post;
import com.example.lab5.model.Topic;
import com.example.lab5.model.UserAccount;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicService {
  private TopicRepository topicRepository;
  private UserAccountService userAccountService;

  private PostRepository postRepository;

  public List<Post> findAllByTopic(Topic topic) {
    return postRepository.findAllByTopic(topic);
  }

  @Autowired
  public void setPostRepository(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Autowired
  public void setUserAccountService(UserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }

  @Autowired
  public void setTopicRepository(TopicRepository topicRepository) {
    this.topicRepository = topicRepository;
  }

  public List<Topic> findAll() {
    return topicRepository.findAll();
  }

  public Page<Topic> findAll(int pageSize, int pageNumber) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return topicRepository.findAll(pageable);
  }

  public List<Topic> filterByNameLength(int maxLength, List<Topic> topicsToFilter) {
    return topicsToFilter.stream()
        .filter(topic -> topic.getTopicName().length() <= maxLength)
        .toList();
  }

  public Optional<Topic> findById(Long id) {
    return topicRepository.findById(id);
  }


  @Transactional
  public Topic save(Topic topic) {
    return userAccountService.findByEmail(topic.getCreator().getEmail())
        .filter(UserAccount::isAdmin)
        .map(userAccount -> {
          Topic newTopic = new Topic(topic.getTopicName(),
              topic.getDescription(),
              userAccount
          );
          return topicRepository.save(newTopic);
        }).orElseThrow(() -> new AccessForbiddenException("User is not admin to create topic!"));
  }


  public void deleteById(Long id) {
    topicRepository.deleteById(id);
  }

  public List<Topic> findAllByUserAccount(UserAccount userAccount) {
    return topicRepository.findAllByCreator(userAccount);
  }
}
