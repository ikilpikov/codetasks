package ru.sber.codetasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.codetasks.domain.Solution;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
}
