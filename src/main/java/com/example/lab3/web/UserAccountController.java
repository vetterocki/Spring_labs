package com.example.lab3.web;

import com.example.lab3.model.AdminAccount;
import com.example.lab3.model.UserAccount;
import com.example.lab3.service.UserAccountService;
import com.example.lab3.web.dto.PostViewDto;
import com.example.lab3.web.dto.TopicViewDto;
import com.example.lab3.web.dto.user.UserModifyDto;
import com.example.lab3.web.dto.user.UserAccountViewDto;
import com.example.lab3.web.dto.user.UserViewDto;
import com.example.lab3.web.mapper.PostMapper;
import com.example.lab3.web.mapper.TopicMapper;
import com.example.lab3.web.mapper.UserAccountMapper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserAccountController {
  private final UserAccountService userAccountService;
  private final UserAccountMapper userAccountMapper;
  private final PostMapper postMapper;
  private final TopicMapper topicMapper;

  @GetMapping("/all")
  public ResponseEntity<List<UserViewDto>> findAll() {
    return userAccountService.findAll().stream()
        .map(userAccountMapper::toDto)
        .collect(Collectors.collectingAndThen(Collectors.toList(), ResponseEntity::ok));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserViewDto> findCreationForm(@PathVariable Long id) {
    return ResponseEntity.of(userAccountService.findById(id).map(userAccountMapper::toDto));
  }

  @GetMapping("/{id}/posts")
  public ResponseEntity<List<PostViewDto>> findAllPostsByUser(@PathVariable Long id) {
    return ResponseEntity.of(userAccountService.findById(id).map(
        userAccount -> userAccount.getPosts().stream()
            .map(postMapper::toDto)
            .toList()));
  }

  @GetMapping("/{id}/topics")
  public ResponseEntity<List<TopicViewDto>> findAllTopicsByUser(@PathVariable Long id) {
    return ResponseEntity.of(userAccountService.findById(id)
        .filter(userAccountService::isUserAdmin)
        .map(userAccount -> (AdminAccount) userAccount)
        .map(adminAccount -> adminAccount.getTopics().stream()
            .map(topicMapper::toDto)
            .toList()));

  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserViewDto> findUpdateForm(@PathVariable Long id,
                                                           @Valid @RequestBody
                                                           UserModifyDto modifyDto) {
    UserAccount modified = userAccountMapper.toEntity(modifyDto);
    UserAccount updated = userAccountService.update(id, modified);
    return ResponseEntity.ok(userAccountMapper.toDto(updated));
  }

  @PostMapping
  public ResponseEntity<UserViewDto> create(@Valid @RequestBody UserModifyDto userAccount) {
    UserAccount created = userAccountService.register(userAccountMapper.toEntity(userAccount));
    return ResponseEntity.status(HttpStatus.CREATED).body(userAccountMapper.toDto(created));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userAccountService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
