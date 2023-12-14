package com.example.lab3.web;

import com.example.lab3.model.Topic;
import com.example.lab3.service.TopicService;
import com.example.lab3.web.dto.ExceptionResponse;
import com.example.lab3.web.dto.PostViewDto;
import com.example.lab3.web.dto.TopicModifyDto;
import com.example.lab3.web.dto.TopicViewDto;
import com.example.lab3.web.dto.user.AdminAccountViewDto;
import com.example.lab3.web.dto.user.UserAccountViewDto;
import com.example.lab3.web.mapper.PostMapper;
import com.example.lab3.web.mapper.TopicMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/topics")
@Tag(name = "Topic Controller", description = "Topics operations")
@RestController
public class TopicController {
  private final TopicService topicService;
  private final TopicMapper topicMapper;
  private final PostMapper postMapper;

  @GetMapping("/all")
  @Operation(summary = "Get all topics wuth pagination", responses = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(schema = @Schema(implementation = TopicViewDto.class))
          })
  })
  public ResponseEntity<List<TopicViewDto>> findAll(@RequestParam int pageSize,
                                                    @RequestParam int pageNumber) {
    return topicService.findAll(pageSize, pageNumber).stream()
        .map(topicMapper::toDto)
        .collect(Collectors.collectingAndThen(Collectors.toList(), ResponseEntity::ok));
  }

  @GetMapping("/all/filtered")
  @Operation(
      summary = "Get all topics filtered by length",
      responses = {
          @ApiResponse(responseCode = "200",
              content = {
                  @Content(schema = @Schema(implementation = TopicViewDto.class))
              }),
      })
  public ResponseEntity<List<TopicViewDto>> findAllFiltered(@RequestParam int maxLength) {
    var list = topicService.findAll();
    return topicService.filterByNameLength(maxLength, list).stream()
        .map(topicMapper::toDto)
        .collect(Collectors.collectingAndThen(Collectors.toList(), ResponseEntity::ok));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get topic by id", responses = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(schema = @Schema(implementation = TopicViewDto.class))
          }),
      @ApiResponse(responseCode = "404", description = "Topic not found",
          content = @Content)
  })
  public ResponseEntity<TopicViewDto> findById(@PathVariable Long id) {
    return ResponseEntity.of(topicService.findById(id).map(topicMapper::toDto));
  }

  @GetMapping("/{id}/posts")
  @Operation(
      summary = "Get all posts that belongs to topic",
      responses = {
          @ApiResponse(responseCode = "200",
              content = {
                  @Content(schema = @Schema(implementation = PostViewDto.class))
              }),
          @ApiResponse(responseCode = "404", description = "Topic does not have any posts",
              content = @Content)
      })
  public ResponseEntity<List<PostViewDto>> findAllPostsByTopic(@PathVariable Long id) {
    return ResponseEntity.of(topicService.findById(id)
        .map(topic -> topic.getPosts().stream()
            .map(postMapper::toDto)
            .toList()));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update topic", responses = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(schema = @Schema(implementation = TopicViewDto.class))
          }),
      @ApiResponse(responseCode = "404", description = "Topic not found by id")
  })
  public ResponseEntity<TopicViewDto> update(@Valid @RequestBody TopicModifyDto topicModifyDto,
                                             @PathVariable Long id) {
    return ResponseEntity.of(topicService.findById(id)
        .map(topic -> topicMapper.partial(topicModifyDto, topic))
        .map(topicService::save)
        .map(topicMapper::toDto));
  }

  @PostMapping
  @Operation(
      summary = "Create topic",
      responses = {
          @ApiResponse(responseCode = "201",
              content = {
                  @Content(schema = @Schema(implementation = TopicViewDto.class))
              }),
          @ApiResponse(responseCode = "403",
              description = "User is not present or is not admin to create topic",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
      })
  public ResponseEntity<TopicViewDto> create(@Valid @RequestBody TopicModifyDto topicModifyDto) {
    Topic topic = topicService.save(topicMapper.toEntity(topicModifyDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(topicMapper.toDto(topic));
  }


  @DeleteMapping("/{id}")
  @Operation(summary = "Delete topic by id", responses = {
      @ApiResponse(responseCode = "204"),
      @ApiResponse(responseCode = "404", description = "Topic not found",
          content = @Content)
  })
  public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
    topicService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
