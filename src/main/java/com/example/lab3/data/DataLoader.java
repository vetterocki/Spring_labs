package com.example.lab3.data;

import static com.example.lab3.model.PostType.BEAUTY;
import static com.example.lab3.model.PostType.ENGINEERING;
import static com.example.lab3.model.PostType.SOCIETY;
import static com.example.lab3.model.PostType.TRAVELS;

import com.example.lab3.model.AdminAccount;
import com.example.lab3.model.Post;
import com.example.lab3.model.Topic;
import com.example.lab3.model.UserAccount;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader {
  private static final String LOREM_IPSUM = """
      Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin dui odio,\040
      lacinia nec varius id, iaculis ut libero. Morbi maximus enim mi, ut fringilla lorem\040
      cursus a. Class aptent taciti.\s
      """;

  private final List<StubCrudRepository<?, ?>> stubCrudRepositories;

  @Bean
  @Scope("prototype")
  CommandLineRunner commandLineRunner() {
    return args -> {
      var user = new UserAccount("user_email@email.com", "password");
      var admin = new AdminAccount("admin_email@email.com", "password");
      repositoryByType(UserAccountRepository.class).saveAll(List.of(user, admin));

      var firstTopic = new Topic("First topic", "Lorem Ipsum first Paragraph", admin);
      var secondTopic = new Topic("Second topic", "Lorem Ipsum second Paragraph", admin);
      var thirdTopic = new Topic("Third topic", "Lorem Ipsum third Paragraph", admin);

      var topics = repositoryByType(TopicRepository.class).saveAll(List.of(firstTopic, secondTopic, thirdTopic));

      final String firstPost = "First post";
      final String secondPost = "Second post";

      var posts = repositoryByType(PostRepository.class).saveAll(
          List.of(new Post(firstPost, user, LOREM_IPSUM, firstTopic, ENGINEERING),
              new Post(secondPost, user, LOREM_IPSUM, firstTopic, SOCIETY),
              new Post(firstPost, user, LOREM_IPSUM, secondTopic, TRAVELS),
              new Post(secondPost, user, LOREM_IPSUM, secondTopic, ENGINEERING),
              new Post(firstPost, user, LOREM_IPSUM, thirdTopic, BEAUTY),
              new Post(secondPost, user, LOREM_IPSUM, thirdTopic, SOCIETY)));

      user.addAllPosts(posts);
      admin.addAllTopics(topics);
      repositoryByType(UserAccountRepository.class).saveAll(List.of(user, admin));
    };


  }

  private <T extends StubCrudRepository<?, ?>> T repositoryByType(Class<T> repositoryType) {
    return stubCrudRepositories.stream()
        .filter(repositoryType::isInstance)
        .map(repositoryType::cast)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }

}
