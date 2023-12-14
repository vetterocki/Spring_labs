package com.example.lab3.service;

import com.example.lab3.data.PostRepository;
import com.example.lab3.exception.TopicNotFoundException;
import com.example.lab3.exception.UserNotFoundException;
import com.example.lab3.model.Post;
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
          Post post = new Post(entity.getPostName(), userAccount, entity.getContent(), topic, entity.getPostType());
          return postRepository.save(post);
        })).orElseThrow(() -> new UserNotFoundException(entity.getCreator().getEmail()))
        .orElseThrow(() -> new TopicNotFoundException(entity.getTopic().getId()));
  }


}

