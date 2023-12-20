package com.example.lab5.data;

import com.example.lab5.model.Topic;
import com.example.lab5.model.UserAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
  @Query("SELECT t FROM Topic t LEFT JOIN UserAccount u ON u = t.creator WHERE u = :creator")
  List<Topic> findAllByCreator(UserAccount creator);

}
