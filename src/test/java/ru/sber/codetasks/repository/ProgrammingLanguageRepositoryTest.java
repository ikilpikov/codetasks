package ru.sber.codetasks.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.sber.codetasks.domain.ProgrammingLanguage;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProgrammingLanguageRepositoryTest {

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    public void tearDown() {
        entityManager.clear();
    }

    @Test
    public void find_language_by_name() {
        String language = "java";
        Optional<ProgrammingLanguage> foundLanguage = programmingLanguageRepository.findByName(language);

        assertTrue(foundLanguage.isPresent());
        assertEquals(language, foundLanguage.get().getName());
    }

    @Test
    public void find_language_by_id() {
        String language = "java";
        Long id = 1L;

        Optional<ProgrammingLanguage> foundLanguage = programmingLanguageRepository.findById(id);

        assertTrue(foundLanguage.isPresent());
        assertEquals(language, foundLanguage.get().getName());
    }

    @Test
    public void save_language() {
        String language = "python";
        ProgrammingLanguage languageEntity = new ProgrammingLanguage();
        languageEntity.setName(language);

        var savedLanguage = programmingLanguageRepository.save(languageEntity);

        assertEquals(language, savedLanguage.getName());
    }

    @Test
    public void delete_language_by_id() {
        Long id = 1L;

        programmingLanguageRepository.deleteById(id);

        assertFalse(programmingLanguageRepository.existsById(id));
    }


}