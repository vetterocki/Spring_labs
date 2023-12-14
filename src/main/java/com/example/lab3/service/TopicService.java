package com.example.lab3.service;

import com.example.lab3.data.TopicRepository;
import com.example.lab3.model.AdminAccount;
import com.example.lab3.model.Topic;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {
  private TopicRepository topicRepository;
  private UserAccountService userAccountService;

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

  public List<Topic> findAll(int pageSize, int pageNumber) {
    var total = topicRepository.findAll();
    int minIndex = pageNumber * pageSize - pageSize;
    int maxIndex = minIndex + pageSize;
    return IntStream.range(0, total.size())
        .filter(i -> i >= minIndex && i <= maxIndex)
        .mapToObj(total::get)
        .collect(Collectors.toList());
  }

  public List<Topic> filterByNameLength(int maxLength, List<Topic> topicsToFilter) {
    return topicsToFilter.stream()
        .filter(topic -> topic.getTopicName().length() <= maxLength)
        .toList();
  }

  public Optional<Topic> findById(Long id) {
    return topicRepository.findById(id);
  }


  public Topic save(Topic topic) {
    return userAccountService.findByEmail(topic.getCreator().getEmail())
        .filter(userAccountService::isUserAdmin).map(userAccount -> {
          Topic newTopic =
              new Topic(topic.getTopicName(), topic.getDescription(), (AdminAccount) userAccount);
          return topicRepository.save(newTopic);
        }).orElseThrow(IllegalArgumentException::new);
  }


  public void deleteById(Long id) {
    topicRepository.deleteById(id);
  }

}
