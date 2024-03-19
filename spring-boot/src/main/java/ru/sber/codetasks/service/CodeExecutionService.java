package ru.sber.codetasks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;

public interface CodeExecutionService {
    ExecutionResultDto executeCode(ExecutionRequestDto executionRequestDto) throws JsonProcessingException;
}
