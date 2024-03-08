package ru.sber.codetasks.service.implementation;

import org.springframework.stereotype.Service;
import ru.sber.codetasks.domain.ProgrammingLanguage;
import ru.sber.codetasks.dto.programming_language.ProgrammingLanguageDto;
import ru.sber.codetasks.dto.programming_language.ReducedProgrammingLanguageDto;
import ru.sber.codetasks.repository.ProgrammingLanguageRepository;
import ru.sber.codetasks.service.ProgrammingLanguageService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgrammingLanguageServiceImpl implements ProgrammingLanguageService {
    private final ProgrammingLanguageRepository programmingLanguageRepository;

    public static final String LANGUAGE_NOT_FOUND_MESSAGE = "Programming language not found: ";

    public ProgrammingLanguageServiceImpl(ProgrammingLanguageRepository programmingLanguageRepository) {
        this.programmingLanguageRepository = programmingLanguageRepository;
    }

    @Override
    public void createProgrammingLanguage(ReducedProgrammingLanguageDto programmingLanguageDto) {
        var programmingLanguage = new ProgrammingLanguage();
        programmingLanguage.setName(programmingLanguageDto.getName());
        programmingLanguageRepository.save(programmingLanguage);
    }

    @Override
    public void deleteProgrammingLanguage(Long id) {
        if (!programmingLanguageRepository.existsById(id)) {
            throw new EntityNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE + id);
        }
        programmingLanguageRepository.deleteById(id);
    }

    @Override
    public ReducedProgrammingLanguageDto getProgrammingLanguage(Long id) {
        var programmingLanguage = programmingLanguageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE + id));

        return new ReducedProgrammingLanguageDto(programmingLanguage.getName());
    }

    @Override
    public List<ProgrammingLanguageDto> getProgrammingLanguages() {
        return programmingLanguageRepository.findAll()
                .stream()
                .map(programmingLanguage -> new ProgrammingLanguageDto(programmingLanguage.getId(),
                        programmingLanguage.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateProgrammingLanguage(Long id, ReducedProgrammingLanguageDto programmingLanguageDto) {
        var programmingLanguage = programmingLanguageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE + id));

        programmingLanguage.setName(programmingLanguageDto.getName());
        programmingLanguageRepository.save(programmingLanguage);
    }

}
