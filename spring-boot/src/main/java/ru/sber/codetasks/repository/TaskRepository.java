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
    @Query("SELECT t FROM Task t " +
            "WHERE (COALESCE(:difficulties) IS NULL OR t.difficulty IN :difficulties) " +
            "AND (COALESCE(:topics) IS NULL OR t.topic.name IN :topics) " +
            "AND (COALESCE(:languages) IS NULL OR EXISTS (select 1 FROM t.languages where name in :languages)) ") //t.languages.name IN :langs

    List<Task> findByCriteria(@Param("difficulties") List<Difficulty> difficulties,
                              @Param("topics") List<String> topics,
                              @Param("languages") List<String> languages,
                              Pageable pageable);
}