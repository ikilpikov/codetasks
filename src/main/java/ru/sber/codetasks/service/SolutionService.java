package ru.sber.codetasks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;
import ru.sber.codetasks.dto.solution.SolutionAttemptDto;
import ru.sber.codetasks.dto.solution.SolutionResponseDto;

public interface SolutionService {
    ExecutionResultDto executeSolutionCode(
            ExecutionRequestDto executionRequestDto) throws JsonProcessingException;

    SolutionResponseDto solutionAttempt(SolutionAttemptDto solutionAttemptDto,
                                               String username) throws JsonProcessingException;

}
