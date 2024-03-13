package ru.sber.codetasks.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import ru.sber.codetasks.domain.Solution;
import ru.sber.codetasks.domain.TestCase;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;
import ru.sber.codetasks.dto.solution.SolutionAttemptDto;
import ru.sber.codetasks.dto.solution.SolutionResponseDto;
import ru.sber.codetasks.repository.SolutionRepository;
import ru.sber.codetasks.repository.TaskRepository;
import ru.sber.codetasks.repository.UserRepository;
import ru.sber.codetasks.service.CodeExecutionService;
import ru.sber.codetasks.service.SolutionService;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class SolutionServiceImpl implements SolutionService {

    private final CodeExecutionService codeExecutionService;

    private final TaskRepository taskRepository;

    private final SolutionRepository solutionRepository;

    private final UserRepository userRepository;

    public static final String TASK_NOT_FOUND_MESSAGE = "Task not found: ";

    public static final String USER_NOT_FOUND_MESSAGE = "User not found ";

    public static final String SUCCESS_MESSAGE = "Solution accepted";

    public static final String FAIL_MESSAGE = "Solution not accepted";

    public SolutionServiceImpl(CodeExecutionService codeExecutionService,
                               TaskRepository taskRepository,
                               SolutionRepository solutionRepository,
                               UserRepository userRepository) {
        this.codeExecutionService = codeExecutionService;
        this.taskRepository = taskRepository;
        this.solutionRepository = solutionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ExecutionResultDto executeSolutionCode(
            ExecutionRequestDto executionRequestDto) throws JsonProcessingException {
        return codeExecutionService.executeCode(executionRequestDto);
    }

    @Override
    public SolutionResponseDto solutionAttempt(SolutionAttemptDto solutionAttemptDto,
                                               String username) throws JsonProcessingException {
        var task = taskRepository
                .findById(solutionAttemptDto.getTaskId())
                .orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE +
                        solutionAttemptDto.getTaskId()));

        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + username));

        var testCases = task.getTestCases();

        var response = new SolutionResponseDto();
        var executionTime = executeTestCases(testCases, solutionAttemptDto);

        if (executionTime == -1) {
            response.setStatus(FAIL_MESSAGE);
            response.setTime(0);
            return response;
        }

        Solution solution = new Solution();
        solution.setCode(solutionAttemptDto.getCode());
        solution.setTask(task);
        solution.setUser(user);
        solution.setExecutionTime(executionTime);
        solution.setSubmissionDate(new Timestamp(System.currentTimeMillis()));
        solutionRepository.save(solution);

        response.setStatus(SUCCESS_MESSAGE);
        response.setTime(executionTime);
        return response;
    }

    private ExecutionRequestDto map(SolutionAttemptDto solutionAttemptDto, String stdin) {
        var executionRequestDto = new ExecutionRequestDto();
        executionRequestDto.setCode(solutionAttemptDto.getCode());
        executionRequestDto.setStdin(stdin);
        executionRequestDto.setLanguage(solutionAttemptDto.getLanguage());
        return executionRequestDto;
    }

    private int executeTestCases(List<TestCase> testCases,
                                 SolutionAttemptDto solutionAttemptDto) throws JsonProcessingException {

        var executionTime = 0;

        for (var testcase : testCases) {
            var executionResult = codeExecutionService.executeCode(map(solutionAttemptDto, testcase.getInputData()));
            if (!executionResult.getStdout().trim().equals(testcase.getOutputData())) {
                return -1;
            }
            executionTime += executionResult.getExecutionTime();
        }

        return executionTime;
    }

}
