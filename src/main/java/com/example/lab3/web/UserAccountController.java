package com.example.lab3.web;

import com.example.lab3.model.AdminAccount;
import com.example.lab3.model.UserAccount;
import com.example.lab3.service.UserAccountService;
import com.example.lab3.web.dto.ExceptionResponse;
import com.example.lab3.web.dto.PostViewDto;
import com.example.lab3.web.dto.TopicViewDto;
import com.example.lab3.web.dto.user.AdminAccountViewDto;
import com.example.lab3.web.dto.user.UserModifyDto;
import com.example.lab3.web.dto.user.UserAccountViewDto;
import com.example.lab3.web.dto.user.UserViewDto;
import com.example.lab3.web.mapper.PostMapper;
import com.example.lab3.web.mapper.TopicMapper;
import com.example.lab3.web.mapper.UserAccountMapper;
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
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "User account Controller", description = "Users and admins operations")
@RequestMapping("/users")
@RestController
public class UserAccountController {
  private final UserAccountService userAccountService;
  private final UserAccountMapper userAccountMapper;
  private final PostMapper postMapper;
  private final TopicMapper topicMapper;

  @GetMapping("/all")
  @Operation(summary = "Get all users", responses = {
      @ApiResponse(responseCode = "200",
          description = "Depending on object type in DB, corresponding view dto type returns",
          content = {
              @Content(schema = @Schema(
                  oneOf = {
                      AdminAccountViewDto.class,
                      UserAccountViewDto.class
                  }))
          })
  })
  public ResponseEntity<List<UserViewDto>> findAll() {
    return userAccountService.findAll().stream()
        .map(userAccountMapper::toDto)
        .collect(Collectors.collectingAndThen(Collectors.toList(), ResponseEntity::ok));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get user by id", responses = {
      @ApiResponse(responseCode = "200",
          description = "Depending on ID param, corresponding view dto type returns",
          content = {
              @Content(schema = @Schema(oneOf = {
                  AdminAccountViewDto.class,
                  UserAccountViewDto.class
              }))
          }),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content)
  })
  public ResponseEntity<UserViewDto> findCreationForm(@PathVariable Long id) {
    return ResponseEntity.of(userAccountService.findById(id).map(userAccountMapper::toDto));
  }

  @GetMapping("/{id}/posts")
  @Operation(
      summary = "Get all posts that belong user",
      responses = {
          @ApiResponse(responseCode = "200",
              content = {
                  @Content(schema = @Schema(implementation = PostViewDto.class))
              }),
          @ApiResponse(responseCode = "404", description = "User not found",
              content = @Content)
      })
  public ResponseEntity<List<PostViewDto>> findAllPostsByUser(@PathVariable Long id) {
    return ResponseEntity.of(userAccountService.findById(id).map(
        userAccount -> userAccount.getPosts().stream()
            .map(postMapper::toDto)
            .toList()));
  }

  @GetMapping("/{id}/topics")
  @Operation(
      summary = "Get all topics that belong user",
      responses = {
          @ApiResponse(responseCode = "200",
              content = {
                  @Content(schema = @Schema(implementation = TopicViewDto.class))
              }),
          @ApiResponse(responseCode = "404", description = "User not found",
              content = @Content)
      })
  public ResponseEntity<List<TopicViewDto>> findAllTopicsByUser(@PathVariable Long id) {
    return ResponseEntity.of(userAccountService.findById(id)
        .filter(userAccountService::isUserAdmin)
        .map(userAccount -> (AdminAccount) userAccount)
        .map(adminAccount -> adminAccount.getTopics().stream()
            .map(topicMapper::toDto)
            .toList()));

  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update user account, (USER OR ADMIN)", responses = {
      @ApiResponse(responseCode = "200",
          description = "Depending on modify dto type, corresponding view dto type returns",
          content = {
              @Content(schema = @Schema(oneOf = {
                  AdminAccountViewDto.class,
                  UserAccountViewDto.class
              }))
          }),
      @ApiResponse(responseCode = "404", description = "User not found by id"),
      @ApiResponse(responseCode = "400",
          description = "Another user with such email already exists",
          content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
  })
  public ResponseEntity<UserViewDto> findUpdateForm(@PathVariable Long id,
                                                    @RequestBody UserModifyDto modifyDto) {
    return ResponseEntity.of(userAccountService.findById(id)
        .map(userAccount -> userAccountService.update(id, userAccountMapper.toEntity(modifyDto)))
        .map(userAccountMapper::toDto));
  }

  @PostMapping
  @Operation(
      summary = "Create user account (USER OR ADMIN)",
      responses = {
          @ApiResponse(responseCode = "201",
              description = "Depending on modify dto type, corresponding view dto type returns",
              content = {
                  @Content(schema = @Schema(oneOf = {
                      AdminAccountViewDto.class,
                      UserAccountViewDto.class
                  }))
              }),
          @ApiResponse(responseCode = "400",
              description = "Another user with such email already exists",
              content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
      })
  public ResponseEntity<UserViewDto> create(@RequestBody UserModifyDto userAccount) {
    UserAccount created = userAccountService.register(userAccountMapper.toEntity(userAccount));
    return ResponseEntity.status(HttpStatus.CREATED).body(userAccountMapper.toDto(created));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete user by id", responses = {
      @ApiResponse(responseCode = "204"),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content)
  })
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userAccountService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
