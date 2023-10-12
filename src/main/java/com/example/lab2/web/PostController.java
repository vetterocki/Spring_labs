package com.example.lab2.web;


import com.example.lab2.model.Post;
import com.example.lab2.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/posts")
@Controller
public class PostController {
  private static final String POST_ATTRIBUTE = "post";
  private final PostService postService;

  @GetMapping("/all")
  public String findAll(Model model) {
    model.addAttribute("posts", postService.findAll());
    return "/posts/all";
  }

  @GetMapping("/creation-form")
  public String findCreationForm(Post post, Model model) {
    model.addAttribute(POST_ATTRIBUTE, post);
    return "/posts/creation";
  }

  @GetMapping("/update/{id}")
  public String findUpdateForm(@PathVariable Long id, Model model) {
    postService.findById(id).ifPresent(found -> model.addAttribute(POST_ATTRIBUTE, found));
    return "/posts/changing";
  }

  @PostMapping("/update/{id}")
  public String update(Post post, Model model, @PathVariable Long id) {
    postService.partialUpdate(id, post);
    return findAll(model);
  }

  @PostMapping("/create")
  public String create(Post post, Model model) {
    postService.save(post);
    return findAll(model);
  }

  @GetMapping("/delete/{id}")
  public String deleteTopic(Model model, @PathVariable Long id) {
    postService.deleteById(id);
    return findAll(model);
  }
}
