package com.example.lab2.service;

import static com.example.lab2.service.PartialUpdateUtils.updateIfNotNull;
import static com.example.lab2.service.PartialUpdateUtils.updateIfNotNullAndNotEmpty;

import com.example.lab2.data.PostRepository;
import com.example.lab2.model.Post;
import com.example.lab2.model.Topic;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
  @Autowired
  private PostRepository postRepository;

  @Autowired
  private TopicService topicService;

  @Autowired
  private UserAccountService userAccountService;

  public List<Post> findAll() {
    return postRepository.findAll();
  }

  public Optional<Post> findById(Long id) {
    return postRepository.findById(id);
  }

  public void deleteById(Long id) {
    postRepository.deleteById(id);
  }

  public Post save(Post entity) {
    return topicService.findById(entity.getTopic().getId())
        .map(topic -> userAccountService.findByEmail(entity.getCreator().getEmail()).map(userAccount -> {
          Post post = new Post(entity.getPostName(), userAccount, entity.getContent(), topic);
          return postRepository.save(post);
        })).orElseThrow(IllegalArgumentException::new)
        .orElseThrow(IllegalArgumentException::new);
  }

  public Post partialUpdate(Long id, Post updated) {
    return findById(id)
        .map(post -> userAccountService.findByEmail(post.getCreator().getEmail())
            .map(userAccount -> topicService.findById(updated.getTopic().getId())
                .map(topic -> {
                  updateIfNotNullAndNotEmpty(updated.getPostName(), post::setPostName);
                  updateIfNotNull(updated.getCreator(), post::setCreator);
                  updateIfNotNull(updated.getTopic(), post::setTopic);
                  updateIfNotNullAndNotEmpty(updated.getContent(), post::setContent);
                  return postRepository.save(post);
                })
                .orElseThrow(IllegalArgumentException::new))
            .orElseThrow(IllegalArgumentException::new))
        .orElseThrow(IllegalArgumentException::new);
  }


  public List<Post> findAllByTopic(Topic topic) {
    return postRepository.findAllByTopic(topic);
  }
}

