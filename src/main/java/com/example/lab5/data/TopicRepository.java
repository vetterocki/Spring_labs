package com.example.lab5.data;

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
public class TopicRepository implements JdbcRepository<Long, Topic> {
  private static final String SELECT_ALL = "select * from topics";
  private static final String SELECT_BY_ID = "select * from topics where id = ?";
  private static final String SELECT_BY_TOPIC_NAME = "select * from topics where topicName = ?";
  private static final String DELETE_BY_ID = "delete from topics where id = ?";
  private static final String INSERT =
      "insert into topics (topicName, createdAt, description, creator_id, isArchived) values (?, ?, ?, ?, ?)";
  private static final String UPDATE =
      "update topics set topicName = ?, createdAt = ?, description = ?, creator_id = ?, isArchived = ? where id = ?";

  private static final String JOIN_USER_ACCOUNT =
      "select * from topics left join user_accounts u on u.id = creator_id where creator_id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final JdbcRepository<Long, UserAccount> userAccountRepository;

  @Override
  public List<Topic> findAll() {
    return jdbcTemplate.query(SELECT_ALL, (rs, row) -> mapFromResultSet(rs));
  }

  @Override
  public Optional<Topic> findById(Long id) {
    return jdbcTemplate.query(SELECT_BY_ID,
        rs -> rs.next() ? Optional.of(mapFromResultSet(rs)) : Optional.empty(), id);
  }

  public Optional<Topic> findByTopicName(String topicName) {
    return jdbcTemplate.query(SELECT_BY_TOPIC_NAME,
        rs -> rs.next() ? Optional.of(mapFromResultSet(rs)) : Optional.empty(), topicName);
  }

  @Override
  public void deleteById(Long id) {
    jdbcTemplate.update(DELETE_BY_ID, id);
  }

  @Override
  public Topic save(Topic entity) {
    final Long id = entity.getId();
    findById(id)
        .ifPresentOrElse(
            presentInDb -> jdbcTemplate.update(UPDATE, entity.getTopicName(), entity.getCreatedAt(),
                entity.getDescription(), entity.getCreator().getId(), entity.isArchived(), id),
            () -> jdbcTemplate.update(INSERT, entity.getTopicName(), entity.getCreatedAt(),
                entity.getDescription(), entity.getCreator().getId(), entity.isArchived()
            ));
    return findByTopicName(entity.getTopicName()).orElse(null);
  }

  @Override
  public List<Topic> saveAll(Collection<Topic> entities) {
    return entities.stream()
        .map(this::save)
        .toList();
  }

  @Override
  public Topic mapFromResultSet(ResultSet resultSet) throws SQLException {
    Topic topic = new Topic();
    topic.setId(resultSet.getLong("id"));
    topic.setTopicName(resultSet.getString("topicName"));
    topic.setArchived(resultSet.getBoolean("isArchived"));
    topic.setDescription(resultSet.getString("description"));
    topic.setCreatedAt(resultSet.getObject(3, LocalDateTime.class));
    topic.setCreator(userAccountRepository.findById(resultSet.getLong("creator_id")).orElse(null));
    return topic;
  }

  public List<Topic> findAllByUserAccount(UserAccount userAccount) {
    return jdbcTemplate.query(JOIN_USER_ACCOUNT, (rs, row) -> mapFromResultSet(rs),
        userAccount.getId());
  }

}
