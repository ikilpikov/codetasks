package ru.sber.codetasks.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;
import ru.sber.codetasks.dto.solution.SolutionAttemptDto;
import ru.sber.codetasks.dto.solution.SolutionResponseDto;
import ru.sber.codetasks.dto.solution.SolutionUserDto;
import ru.sber.codetasks.service.SolutionService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/solution")
@CrossOrigin(origins = "*")
public class SolutionController {
    private final SolutionService solutionService;

    private static final String FIELDS_INVALID_MESSAGE = "Fields are invalid";

    public SolutionController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    @PostMapping("/execute")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExecutionResultDto> executeCodeWithoutChecks(
            @RequestBody @Valid ExecutionRequestDto executionRequestDto) throws JsonProcessingException {

        var result =  solutionService.executeSolutionCode(executionRequestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/attempt")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SolutionResponseDto> executeCodeWithChecks(
            @RequestBody @Valid SolutionAttemptDto solutionAttemptDto) throws JsonProcessingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var result =  solutionService.solutionAttempt(solutionAttemptDto, authentication.getName());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SolutionUserDto>> getAllSolutionsForTask(@PathVariable Long id) throws AccessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var solutions = solutionService.getAllSolutionsForTask(id, authentication.getName());
        return new ResponseEntity<>(solutions, HttpStatus.OK);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<String> accessExceptionHandler(AccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> invalidDataExceptionHandler(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(FIELDS_INVALID_MESSAGE, HttpStatus.BAD_REQUEST);
    }

}
