package ru.sber.codetasks.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;
import ru.sber.codetasks.service.CodeExecutionService;

@Service
public class SolutionServiceImpl {
    private final CodeExecutionService codeExecutionService;

    public SolutionServiceImpl(CodeExecutionService codeExecutionService) {
        this.codeExecutionService = codeExecutionService;
    }

    public ExecutionResultDto executeSolutionCode(
            ExecutionRequestDto executionRequestDto) throws JsonProcessingException {
        return codeExecutionService.executeCode(executionRequestDto);
    }

}
