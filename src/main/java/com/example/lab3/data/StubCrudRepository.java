package com.example.lab3.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class StubCrudRepository<I, E> {
  protected Long identityCounter = 1L;

  protected List<E> entities = new ArrayList<>();

  public abstract List<E> findAll();
  public abstract Optional<E> findById(I id);
  public abstract void deleteById(I id);
  public abstract E save(E entity);

  public abstract List<E> saveAll(Collection<E> entities);
}
