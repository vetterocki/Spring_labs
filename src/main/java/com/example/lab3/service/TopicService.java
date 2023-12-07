package com.example.lab3.service;

import static com.example.lab3.service.PartialUpdateUtils.updateIfNotNull;
import static com.example.lab3.service.PartialUpdateUtils.updateIfNotNullAndNotEmpty;

import com.example.lab3.data.PostRepository;
import com.example.lab3.data.TopicRepository;
import com.example.lab3.model.AdminAccount;
import com.example.lab3.model.Post;
import com.example.lab3.model.Topic;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {
  private TopicRepository topicRepository;
  private UserAccountService userAccountService;
  private PostRepository postRepository;

  @Autowired
  public void setUserAccountService(UserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }

  @Autowired
  public void setPostRepository(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Autowired
  public void setTopicRepository(TopicRepository topicRepository) {
    this.topicRepository = topicRepository;
  }

  public List<Topic> findAll() {
    return topicRepository.findAll();
  }

  public Optional<Topic> findById(Long id) {
    return topicRepository.findById(id);
  }

  public List<Post> findAllPostsByTopic(Long id) {
    return findById(id).map(topic -> postRepository.findAllByTopic(topic))
        .orElseThrow(IllegalArgumentException::new);
  }

  public Topic save(Topic topic) {
    return userAccountService.findByEmail(topic.getCreator().getEmail())
        .filter(userAccountService::isUserAdmin).map(userAccount -> {
          Topic newTopic =
              new Topic(topic.getTopicName(), topic.getDescription(), (AdminAccount) userAccount);
          return topicRepository.save(newTopic);
        }).orElseThrow(IllegalArgumentException::new);
  }

  public Topic partialUpdate(Long id, Topic updated) {
    return findById(id).map(topic -> userAccountService.findByEmail(topic.getCreator().getEmail())
        .filter(userAccountService::isUserAdmin).map(userAccount -> {
              updateIfNotNull(updated.getCreator(), topic::setCreator);
              updateIfNotNullAndNotEmpty(updated.getTopicName(), topic::setTopicName);
              updateIfNotNullAndNotEmpty(updated.getDescription(), topic::setDescription);
              return topicRepository.save(topic);
        }).orElseThrow(IllegalArgumentException::new))
        .orElseThrow(IllegalArgumentException::new);

  }


  public void deleteById(Long id) {
    topicRepository.deleteById(id);
  }
}
