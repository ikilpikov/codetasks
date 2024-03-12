package ru.sber.codetasks.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;
import ru.sber.codetasks.dto.solution.SolutionAttemptDto;
import ru.sber.codetasks.repository.SolutionRepository;
import ru.sber.codetasks.repository.TaskRepository;
import ru.sber.codetasks.repository.UserRepository;
import ru.sber.codetasks.service.CodeExecutionService;

@Service
public class SolutionServiceImpl {
    private final CodeExecutionService codeExecutionService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    UserRepository userRepository;

    public SolutionServiceImpl(CodeExecutionService codeExecutionService) {
        this.codeExecutionService = codeExecutionService;
    }

    public ExecutionResultDto executeSolutionCode(
            ExecutionRequestDto executionRequestDto) throws JsonProcessingException {
        return codeExecutionService.executeCode(executionRequestDto);
    }

    public String solutionAttempt(SolutionAttemptDto solutionAttemptDto, String username) throws JsonProcessingException {
        var task = taskRepository.findById(solutionAttemptDto.getTaskId()).get();
        var user = userRepository.findByUsername(username).get();
        var testcases = task.getTestCases();

        var executionTime = 0;
        for (var testcase : testcases) {
            var executionResult = codeExecutionService.executeCode(map(solutionAttemptDto, testcase.getInputData()));
            if (!executionResult.getStdout().trim().equals(testcase.getOutputData())) {
                return "not okay";
            }
            executionTime += executionResult.getExecutionTime();
        }

        return "okay " + executionTime;
    }

    private ExecutionRequestDto map(SolutionAttemptDto solutionAttemptDto, String stdin) {
        var executionRequestDto = new ExecutionRequestDto();
        executionRequestDto.setCode(solutionAttemptDto.getCode());
        executionRequestDto.setStdin(stdin);
        executionRequestDto.setLanguage(solutionAttemptDto.getLanguage());
        return executionRequestDto;
    }

}
