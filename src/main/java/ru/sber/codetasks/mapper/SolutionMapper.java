package ru.sber.codetasks.mapper;

import org.springframework.stereotype.Component;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.solution.SolutionAttemptDto;

@Component
public class SolutionMapper {
    public ExecutionRequestDto mapSolutionAttemptDtoToExecutionRequestDto(SolutionAttemptDto solutionAttemptDto,
                                                                          String stdin) {
        var executionRequestDto = new ExecutionRequestDto();
        executionRequestDto.setCode(solutionAttemptDto.getCode());
        executionRequestDto.setStdin(stdin);
        executionRequestDto.setLanguage(solutionAttemptDto.getLanguage());
        return executionRequestDto;
    }

}
