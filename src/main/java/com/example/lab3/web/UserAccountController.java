package com.example.lab3.web;

import com.example.lab3.model.UserAccount;
import com.example.lab3.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserAccountController {
  private final UserAccountService userAccountService;
  private static final String USER_ATTRIBUTE = "user";

  @GetMapping("/all")
  public String findAll(Model model) {
    model.addAttribute("users", userAccountService.findAll());
    return "/users/all";
  }

  @GetMapping("/creation-form")
  public String findCreationForm(UserAccount userAccount, Model model) {
    model.addAttribute(USER_ATTRIBUTE, userAccount);
    return "/users/creation";
  }

  @GetMapping("/posts/{id}")
  public String findAllPostsByUser(Model model, @PathVariable Long id) {
    userAccountService.findById(id).ifPresent(found -> {
      model.addAttribute("posts", userAccountService.findAllPostsByUser(found));
      model.addAttribute(USER_ATTRIBUTE, found);
    });
    return "/posts/all";
  }

  @GetMapping("/topics/{id}")
  public String findAllTopicsByUser(Model model, @PathVariable Long id) {
    userAccountService.findById(id).ifPresent(found -> {
      model.addAttribute("topics", userAccountService.findAllTopicsByUser(found));
      model.addAttribute(USER_ATTRIBUTE, found);
    });
    return "/topics/all";
  }

  @GetMapping("/update/{id}")
  public String findUpdateForm(@PathVariable Long id, Model model) {
    userAccountService.findById(id).ifPresent(found -> model.addAttribute(USER_ATTRIBUTE, found));
    return "/users/changing";
  }

  @PostMapping("/update/{id}")
  public String update(UserAccount userAccount, Model model, @PathVariable Long id) {
    userAccountService.partialUpdate(id, userAccount);
    return findAll(model);
  }

  @PostMapping("/create")
  public String create(UserAccount userAccount, Model model) {
    userAccountService.register(userAccount);
    return findAll(model);
  }

  @GetMapping("/delete/{id}")
  public String delete(Model model, @PathVariable Long id) {
    userAccountService.deleteById(id);
    return findAll(model);
  }
}
