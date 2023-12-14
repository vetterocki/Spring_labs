package com.example.lab3.web;


import com.example.lab3.model.Post;
import com.example.lab3.service.PostService;
import com.example.lab3.web.dto.ExceptionResponse;
import com.example.lab3.web.dto.PostModifyDto;
import com.example.lab3.web.dto.PostViewDto;
import com.example.lab3.web.dto.TopicViewDto;
import com.example.lab3.web.dto.user.AdminAccountViewDto;
import com.example.lab3.web.dto.user.UserAccountViewDto;
import com.example.lab3.web.mapper.PostMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @Operation(summary = "Get all posts", responses = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(schema = @Schema(implementation = PostViewDto.class))
          })
  })
  public ResponseEntity<List<PostViewDto>> findAll() {
    return postService.findAll().stream()
        .map(postMapper::toDto)
        .collect(Collectors.collectingAndThen(Collectors.toList(), ResponseEntity::ok));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get post by id", responses = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(schema = @Schema(implementation = PostViewDto.class))
          }),
      @ApiResponse(responseCode = "404", description = "Post not found",
          content = @Content)
  })
  public ResponseEntity<PostViewDto> findById(@PathVariable Long id) {
    return ResponseEntity.of(postService.findById(id).map(postMapper::toDto));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update post", responses = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(schema = @Schema(implementation = PostViewDto.class))
          }),
      @ApiResponse(responseCode = "404", description = "Post not found by id")
  })
  public ResponseEntity<PostViewDto> update(@Valid @RequestBody PostModifyDto post, @PathVariable Long id) {
    return ResponseEntity.of(postService.findById(id)
        .map(found -> postMapper.partial(post, found))
        .map(postService::save)
        .map(postMapper::toDto));
  }

  @PostMapping
  @Operation(
      summary = "Create post",
      responses = {
          @ApiResponse(responseCode = "201",
              content = {
                  @Content(schema = @Schema(implementation = PostViewDto.class))
              }),
          @ApiResponse(responseCode = "404",
              description = "Either user or topic is not not found by id",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
      })
  public ResponseEntity<PostViewDto> create(@Valid @RequestBody PostModifyDto postModifyDto) {
    Post post = postService.save(postMapper.toEntity(postModifyDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toDto(post));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete post by id", responses = {
      @ApiResponse(responseCode = "204"),
      @ApiResponse(responseCode = "404", description = "Post not found",
          content = @Content)
  })
  public ResponseEntity<Void> deletePost(@PathVariable Long id) {
    postService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
