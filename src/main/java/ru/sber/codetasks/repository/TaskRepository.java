package ru.sber.codetasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.codetasks.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
