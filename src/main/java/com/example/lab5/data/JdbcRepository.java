package com.example.lab5.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface JdbcRepository<I, E> {

  List<E> findAll();
  Optional<E> findById(I id);
  void deleteById(I id);
  E save(E entity);

  List<E> saveAll(Collection<E> entities);

  E mapFromResultSet(ResultSet resultSet) throws SQLException;
}
