package ru.sber.codetasks.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;
import ru.sber.codetasks.domain.Solution;
import ru.sber.codetasks.domain.Task;
import ru.sber.codetasks.domain.TestCase;
import ru.sber.codetasks.domain.User;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;
import ru.sber.codetasks.dto.solution.SolutionAttemptDto;
import ru.sber.codetasks.dto.solution.SolutionResponseDto;
import ru.sber.codetasks.dto.solution.SolutionUserDto;
import ru.sber.codetasks.mapper.SolutionMapper;
import ru.sber.codetasks.repository.SolutionRepository;
import ru.sber.codetasks.repository.TaskRepository;
import ru.sber.codetasks.repository.UserRepository;
import ru.sber.codetasks.service.CodeExecutionService;
import ru.sber.codetasks.service.SolutionService;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolutionServiceImpl implements SolutionService {

    private final CodeExecutionService codeExecutionService;

    private final TaskRepository taskRepository;

    private final SolutionRepository solutionRepository;

    private final UserRepository userRepository;

    private final SolutionMapper solutionMapper;

    public static final String TASK_NOT_FOUND_MESSAGE = "Task not found: ";

    public static final String USER_NOT_FOUND_MESSAGE = "User not found ";

    public static final String SUCCESS_MESSAGE = "Solution accepted ";

    public static final String FAIL_MESSAGE = "Solution not accepted ";

    public static final String NO_RIGHTS_MESSAGE = "Task is not solved ";

    public SolutionServiceImpl(CodeExecutionService codeExecutionService,
                               TaskRepository taskRepository,
                               SolutionRepository solutionRepository,
                               UserRepository userRepository,
                               SolutionMapper solutionMapper) {
        this.codeExecutionService = codeExecutionService;
        this.taskRepository = taskRepository;
        this.solutionRepository = solutionRepository;
        this.userRepository = userRepository;
        this.solutionMapper = solutionMapper;
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

    @Override
    public List<SolutionUserDto> getAllSolutionsForTask(Long taskId, String username) throws AccessException {
        var task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND_MESSAGE + taskId));

        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + username));

        if (!isAllowedCheckingSolutions(task, user)) {
            throw new AccessException(NO_RIGHTS_MESSAGE);
        }

        return task
                .getSolutions()
                .stream()
                .filter(x -> x.getTask().getId().equals(taskId))
                .map(x -> {
                    SolutionUserDto dto = new SolutionUserDto();
                    dto.setId(x.getId());
                    dto.setCode(x.getCode());
                    dto.setUsername(x.getUser().getUsername());
                    dto.setSubmissionDate(x.getSubmissionDate());
                    dto.setSubmissionTime(x.getExecutionTime());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private int executeTestCases(List<TestCase> testCases,
                                 SolutionAttemptDto solutionAttemptDto) throws JsonProcessingException {

        var executionTime = 0;

        for (var testcase : testCases) {
            var executionResult = codeExecutionService.
                    executeCode(solutionMapper
                            .mapSolutionAttemptDtoToExecutionRequestDto(solutionAttemptDto,
                            testcase.getInputData()));

            if (!executionResult.getStdout().trim().equals(testcase.getOutputData())) {
                return -1;
            }
            executionTime += executionResult.getExecutionTime();
        }

        return executionTime;
    }

    private boolean isAllowedCheckingSolutions(Task task, User user) {
        return task
                .getSolutions()
                .stream()
                .map(x -> x.getUser().getId())
                .anyMatch(x -> x.equals(user.getId()));
    }

}
