package ru.sber.codetasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.codetasks.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
