package com.example.lab5.data;

import com.example.lab5.model.Post;
import com.example.lab5.model.PostType;
import com.example.lab5.model.Topic;
import com.example.lab5.model.UserAccount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostRepository implements JdbcRepository<Long, Post> {
  private static final String SELECT_ALL = "select  * from posts";
  private static final String SELECT_BY_ID = "select * from posts where id = ?";
  private static final String SELECT_BY_POST_NAME = "select * from posts where postName = ?";
  private static final String DELETE_BY_ID = "delete from posts where id = ?";
  private static final String INSERT =
      "insert into posts (postName, createdAt, content, creator_id, topic_id, postType) values (?, ?, ?, ?, ?, ?)";
  private static final String UPDATE =
      "update posts set postName = ?, createdAt = ?, content = ?, creator_id = ?, topic_id = ?, postType = ? where id = ?";

  private static final String JOIN_TOPIC =
      "select * from posts left join topics t on topic_id = t.id where topic_id = ?";
  private static final String JOIN_USER_ACCOUNT =
      "select * from posts left join user_accounts u on creator_id = u.id where creator_id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final JdbcRepository<Long, UserAccount> userAccountRepository;
  private final JdbcRepository<Long, Topic> topicRepository;

  @Override
  public List<Post> findAll() {
    return jdbcTemplate.query(SELECT_ALL, (rs, row) -> mapFromResultSet(rs));
  }

  @Override
  public Optional<Post> findById(Long id) {
    return jdbcTemplate.query(SELECT_BY_ID,
        rs -> rs.next() ? Optional.of(mapFromResultSet(rs)) : Optional.empty(), id);
  }

  public Optional<Post> findByPostName(String postName) {
    return jdbcTemplate.query(SELECT_BY_POST_NAME,
        rs -> rs.next() ? Optional.of(mapFromResultSet(rs)) : Optional.empty(), postName);
  }

  @Override
  public void deleteById(Long id) {
    jdbcTemplate.update(DELETE_BY_ID, id);
  }

  @Override
  public Post save(Post entity) {
    final Long id = entity.getId();
    findById(id)
        .ifPresentOrElse(
            presentInDb -> jdbcTemplate.update(UPDATE, entity.getPostName(), entity.getCreatedAt(),
                entity.getContent(), entity.getCreator().getId(), entity.getTopic().getId(),
                entity.getPostType().name(), id),
            () -> jdbcTemplate.update(INSERT, entity.getPostName(), entity.getCreatedAt(),
                entity.getContent(), entity.getCreator().getId(), entity.getTopic().getId(),
                entity.getPostType().name()
            ));
    return findByPostName(entity.getPostName()).orElse(null);
  }

  @Override
  public List<Post> saveAll(Collection<Post> entities) {
    return entities.stream().map(this::save).toList();
  }

  @Override
  public Post mapFromResultSet(ResultSet resultSet) throws SQLException {
    Post post = new Post();
    post.setId(resultSet.getLong("id"));
    post.setPostName(resultSet.getString("postName"));
    post.setPostType(PostType.valueOf(resultSet.getString("postType")));
    post.setContent(resultSet.getString("content"));
    post.setCreatedAt(resultSet.getObject(3, LocalDateTime.class));
    post.setCreator(userAccountRepository.findById(resultSet.getLong("creator_id")).orElse(null));
    post.setTopic(topicRepository.findById(resultSet.getLong("topic_id")).orElse(null));
    return post;
  }

  public List<Post> findAllByTopic(Topic topic) {
    return jdbcTemplate.query(JOIN_TOPIC, (rs, row) -> mapFromResultSet(rs), topic.getId());
  }

  public List<Post> findAllByUserAccount(UserAccount userAccount) {
    return jdbcTemplate.query(JOIN_USER_ACCOUNT, (rs, row) -> mapFromResultSet(rs), userAccount.getId());
  }
}
