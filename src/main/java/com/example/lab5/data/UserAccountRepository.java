package com.example.lab5.data;

import com.example.lab5.model.UserAccount;
import jakarta.persistence.NamedQuery;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
  Optional<UserAccount> findByEmail(String email);

  boolean existsByEmail(String email);

}
