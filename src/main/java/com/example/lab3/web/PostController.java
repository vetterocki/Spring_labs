package com.example.lab3.web;


import com.example.lab3.model.Post;
import com.example.lab3.service.PostService;
import com.example.lab3.web.dto.PostModifyDto;
import com.example.lab3.web.dto.PostViewDto;
import com.example.lab3.web.mapper.PostMapper;
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
@RequestMapping("/posts")
@RestController
public class PostController {
  private final PostService postService;
  private final PostMapper postMapper;

  @GetMapping("/all")
  public ResponseEntity<List<PostViewDto>> findAll() {
    return postService.findAll().stream()
        .map(postMapper::toDto)
        .collect(Collectors.collectingAndThen(Collectors.toList(), ResponseEntity::ok));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostViewDto> findById(@PathVariable Long id) {
    return ResponseEntity.of(postService.findById(id).map(postMapper::toDto));
  }
  @PatchMapping("/{id}")
  public ResponseEntity<PostViewDto> update(@RequestBody PostModifyDto post, @PathVariable Long id) {
    return ResponseEntity.of(postService.findById(id)
        .map(found -> postMapper.partial(post, found))
        .map(postService::save)
        .map(postMapper::toDto));
  }
  @PostMapping
  public ResponseEntity<PostViewDto> create(@Valid @RequestBody PostModifyDto postModifyDto) {
    Post post = postService.save(postMapper.toEntity(postModifyDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toDto(post));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
    postService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
