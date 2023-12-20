package com.example.lab5.data;

import static com.example.lab5.model.PostType.BEAUTY;
import static com.example.lab5.model.PostType.ENGINEERING;
import static com.example.lab5.model.PostType.SOCIETY;
import static com.example.lab5.model.PostType.TRAVELS;

import com.example.lab5.model.Post;
import com.example.lab5.model.Role;
import com.example.lab5.model.Topic;
import com.example.lab5.model.UserAccount;
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

  private final List<JdbcRepository<?, ?>> jdbcRepositories;

  @Bean
  @Scope("prototype")
  CommandLineRunner commandLineRunner() {
    return args -> {
      var userRepo = DataLoader.this.repositoryByType(UserAccountRepository.class);
      if (userRepo.findAll().size() == 0) {
        var user = userRepo.save(new UserAccount("user_email@email.com", "password", Role.USER));
        var admin = userRepo.save(new UserAccount("admin_email@email.com", "password", Role.ADMIN));


        var topicRepo = DataLoader.this.repositoryByType(TopicRepository.class);
        var firstTopic =
            topicRepo.save(new Topic("First topic", "Lorem Ipsum first Paragraph", admin));
        var secondTopic =
            topicRepo.save(new Topic("Second topic", "Lorem Ipsum second Paragraph", admin));
        var thirdTopic =
            topicRepo.save(new Topic("Third topic", "Lorem Ipsum third Paragraph", admin));

        var topics = List.of(firstTopic, secondTopic, thirdTopic);

        var postRepo = DataLoader.this.repositoryByType(PostRepository.class);
        postRepo.saveAll(
            List.of(new Post("Post about planes", user, LOREM_IPSUM, firstTopic, ENGINEERING),
                new Post("Post about joys", user, LOREM_IPSUM, firstTopic, SOCIETY),
                new Post("Post about Austria", user, LOREM_IPSUM, secondTopic, TRAVELS),
                new Post("Post about IT", user, LOREM_IPSUM, secondTopic, ENGINEERING),
                new Post("Post about Gale", user, LOREM_IPSUM, thirdTopic, BEAUTY),
                new Post("Post about politics", user, LOREM_IPSUM, thirdTopic, SOCIETY)));

        user.addAllPosts(postRepo.findAll());
        admin.addAllTopics(topics);
        DataLoader.this.repositoryByType(UserAccountRepository.class).saveAll(List.of(user, admin));
      }
    };


  }

  private <T extends JdbcRepository<?, ?>> T repositoryByType(Class<T> repositoryType) {
    return jdbcRepositories.stream()
        .filter(repositoryType::isInstance)
        .map(repositoryType::cast)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }

}
