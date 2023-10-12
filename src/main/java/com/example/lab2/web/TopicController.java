package com.example.lab2.web;

import com.example.lab2.model.Topic;
import com.example.lab2.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/topics")
@Controller
public class TopicController {
  private static final String TOPIC_ATTRIBUTE = "topic";
  private final TopicService topicService;

  @GetMapping("/all")
  public String findAll(Model model) {
    model.addAttribute("topics", topicService.findAll());
    return "/topics/all";
  }

  @GetMapping("/creation-form")
  public String findCreationForm(Topic topic, Model model) {
    model.addAttribute(TOPIC_ATTRIBUTE, topic);
    return "/topics/creation";
  }

  @GetMapping("/posts/{id}")
  public String findAllPostsByTopic(Model model, @PathVariable Long id) {
    topicService.findById(id).ifPresent(found -> {
      model.addAttribute("posts", topicService.findAllPostsByTopic(id));
      model.addAttribute(TOPIC_ATTRIBUTE, found);
    });
    return "/posts/all";
  }

  @GetMapping("/update/{id}")
  public String findUpdateForm(@PathVariable Long id, Model model) {
    topicService.findById(id).ifPresent(found -> model.addAttribute(TOPIC_ATTRIBUTE, found));
    return "/topics/changing";
  }

  @PostMapping("/update/{id}")
  public String update(Topic topic, Model model, @PathVariable Long id) {
    topicService.partialUpdate(id, topic);
    return findAll(model);
  }

  @PostMapping("/create")
  public String create(Topic topic, Model model) {
    topicService.save(topic);
    return findAll(model);
  }

  @GetMapping("/delete/{id}")
  public String deleteTopic(Model model, @PathVariable Long id) {
    topicService.deleteById(id);
    return findAll(model);
  }
}
