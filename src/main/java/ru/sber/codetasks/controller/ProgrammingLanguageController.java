package ru.sber.codetasks.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.sber.codetasks.dto.programming_language.ProgrammingLanguageDto;
import ru.sber.codetasks.dto.programming_language.ReducedProgrammingLanguageDto;
import ru.sber.codetasks.service.ProgrammingLanguageService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/programming-language")
public class ProgrammingLanguageController {
    private final ProgrammingLanguageService programmingLanguageService;

    public static final String LANGUAGE_ADDED_MESSAGE = "Language added successfully";

    public static final String LANGUAGE_DELETED_MESSAGE = "Language deleted successfully";

    public static final String LANGUAGE_UPDATED_MESSAGE = "Language updated successfully";

    public static final String FIELDS_INVALID_MESSAGE = "Fields are invalid";

    public static final String RELATION_VIOLATION_MESSAGE =
            "Cannot delete cause this language is related to a task";

    public ProgrammingLanguageController(ProgrammingLanguageService programmingLanguageService) {
        this.programmingLanguageService = programmingLanguageService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addProgrammingLanguage(@RequestBody @Valid ReducedProgrammingLanguageDto programmingLanguageDto) {
        programmingLanguageService.createProgrammingLanguage(programmingLanguageDto);
        return new ResponseEntity<>(LANGUAGE_ADDED_MESSAGE, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeProgrammingLanguage(@PathVariable Long id) {
        programmingLanguageService.deleteProgrammingLanguage(id);
        return new ResponseEntity<>(LANGUAGE_DELETED_MESSAGE, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReducedProgrammingLanguageDto> getProgrammingLanguage(@PathVariable Long id) {
        return new ResponseEntity<>(programmingLanguageService.getProgrammingLanguage(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProgrammingLanguageDto>> getAllProgrammingLanguages() {
        var languages = programmingLanguageService.getProgrammingLanguages();
        return new ResponseEntity<>(languages, HttpStatus.OK);
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateProgrammingLanguage(@PathVariable Long id,
                                             @RequestBody @Valid ReducedProgrammingLanguageDto programmingLanguageDto) {

        programmingLanguageService.updateProgrammingLanguage(id, programmingLanguageDto);
        return new ResponseEntity<>(LANGUAGE_UPDATED_MESSAGE, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> invalidDataExceptionHandler() {
        return new ResponseEntity<>(FIELDS_INVALID_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public  ResponseEntity<String> dataIntegrityViolationException() {
        return new ResponseEntity<>(RELATION_VIOLATION_MESSAGE,
                HttpStatus.BAD_REQUEST);
    }

}
