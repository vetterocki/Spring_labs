package com.example.lab5.data;

import com.example.lab5.model.Role;
import com.example.lab5.model.UserAccount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserAccountRepository implements JdbcRepository<Long, UserAccount> {
  private static final String SELECT_ALL = "select * from user_accounts";
  private static final String SELECT_BY_ID = "select * from user_accounts where id = ?";

  private static final String SELECT_BY_EMAIL = "select * from user_accounts where email = ?";
  private static final String DELETE_BY_ID = "delete from user_accounts where id = ?";
  private static final String INSERT =
      "insert into user_accounts (email, password, role) values (?, ?, ?)";
  private static final String UPDATE =
      "update user_accounts set email = ?, password = ?, role = ? where id = ?";

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<UserAccount> findAll() {
    return jdbcTemplate.query(SELECT_ALL, (rs, row) -> mapFromResultSet(rs));
  }

  @Override
  public Optional<UserAccount> findById(Long id) {
    return jdbcTemplate.query(SELECT_BY_ID,
        rs -> rs.next() ? Optional.of(mapFromResultSet(rs)) : Optional.empty(), id);
  }

  @Override
  public void deleteById(Long id) {
    jdbcTemplate.update(DELETE_BY_ID, id);
  }

  @Override
  public UserAccount save(UserAccount entity) {
    final Long id = entity.getId();
    findById(id)
        .ifPresentOrElse(
            presentInDb -> jdbcTemplate.update(UPDATE, entity.getEmail(), entity.getPassword(),
                entity.getRole().name(), id),
            () -> jdbcTemplate.update(INSERT, entity.getEmail(), entity.getPassword(),
                entity.getRole().name()
            ));
    return findByEmail(entity.getEmail()).orElse(null);
  }

  public Optional<UserAccount> findByEmail(String email) {
    return jdbcTemplate.query(SELECT_BY_EMAIL,
        rs -> rs.next() ? Optional.of(mapFromResultSet(rs)) : Optional.empty(), email);
  }

  @Override
  public List<UserAccount> saveAll(Collection<UserAccount> entities) {
    return entities.stream()
        .map(this::save)
        .toList();
  }

  @Override
  public UserAccount mapFromResultSet(ResultSet resultSet) throws SQLException {
    UserAccount userAccount = new UserAccount();
    userAccount.setId(resultSet.getLong("id"));
    userAccount.setEmail(resultSet.getString("email"));
    userAccount.setPassword(resultSet.getString("password"));
    userAccount.setRole(Role.valueOf(resultSet.getString("role")));
    return userAccount;
  }

  public boolean existsByEmail(String email) {
    return Boolean.TRUE.equals(
        jdbcTemplate.query(SELECT_BY_EMAIL, rs -> rs.next() ? true : false, email));
  }

}
