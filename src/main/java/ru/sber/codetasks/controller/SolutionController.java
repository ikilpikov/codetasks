package ru.sber.codetasks.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;
import ru.sber.codetasks.dto.solution.SolutionAttemptDto;
import ru.sber.codetasks.dto.solution.SolutionResponseDto;
import ru.sber.codetasks.service.implementation.SolutionServiceImpl;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/solution")
public class SolutionController {
    private final SolutionServiceImpl solutionService;

    public SolutionController(SolutionServiceImpl solutionService) {
        this.solutionService = solutionService;
    }

    @PostMapping("/execute")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExecutionResultDto> executeCodeWithoutChecks(
            @RequestBody ExecutionRequestDto executionRequestDto) throws JsonProcessingException {
        var result =  solutionService.executeSolutionCode(executionRequestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/attempt")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SolutionResponseDto> executeCodeWithChecks(
            @RequestBody SolutionAttemptDto solutionAttemptDto,
            Authentication authentication) throws JsonProcessingException {
        var result =  solutionService.solutionAttempt(solutionAttemptDto, authentication.getName());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
