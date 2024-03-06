package ru.sber.codetasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.codetasks.domain.TestCase;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    void deleteAllByTask_Id(Long id);
}
