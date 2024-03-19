package ru.sber.codetasks.service;

import ru.sber.codetasks.dto.programming_language.ProgrammingLanguageDto;
import ru.sber.codetasks.dto.programming_language.ReducedProgrammingLanguageDto;

import java.util.List;

public interface ProgrammingLanguageService {
    void createProgrammingLanguage(ReducedProgrammingLanguageDto programmingLanguageDto);

    void deleteProgrammingLanguage(Long id);

    ReducedProgrammingLanguageDto getProgrammingLanguage(Long id);

    List<ProgrammingLanguageDto> getProgrammingLanguages();

    void updateProgrammingLanguage(Long id, ReducedProgrammingLanguageDto programmingLanguageDto);
}
