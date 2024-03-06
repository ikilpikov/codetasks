package ru.sber.codetasks.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sber.codetasks.domain.Task;
import ru.sber.codetasks.domain.enums.Difficulty;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE (:difficulty IS NULL OR t.difficulty = :difficulty) AND " +
            "(:topicName = '' OR t.topic.name = :topicName)")
    List<Task> findByCriteria(@Param("difficulty") Difficulty difficulty, @Param("topicName") String topicName, Pageable pageable);
}
