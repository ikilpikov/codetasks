package ru.sber.codetasks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.expression.AccessException;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;
import ru.sber.codetasks.dto.solution.SolutionAttemptDto;
import ru.sber.codetasks.enums.SolutionResult;
import ru.sber.codetasks.mapper.SolutionMapper;
import ru.sber.codetasks.repository.SolutionRepository;
import ru.sber.codetasks.repository.TaskRepository;
import ru.sber.codetasks.repository.UserRepository;
import ru.sber.codetasks.service.implementation.SolutionServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
class SolutionServiceTest {
    @MockBean
    CodeExecutionService codeExecutionService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    UserRepository userRepository;

    SolutionService solutionService;

    @BeforeEach
    void setUp() {
        SolutionMapper solutionMapper = new SolutionMapper();

        solutionService = new SolutionServiceImpl(codeExecutionService,
                taskRepository,
                solutionRepository,
                userRepository,
                solutionMapper);
    }

    @Test
    void accepted_solution_attempt() throws JsonProcessingException {
        ExecutionResultDto resultDto = getValidExecutionResultDtoWithCorrectSolution();

        when(codeExecutionService.executeCode(any(ExecutionRequestDto.class)))
                .thenReturn(resultDto);

        var result = solutionService.solutionAttempt(getValidSolutionAttemptDto(), "user");
        assertEquals(SolutionResult.ACCEPTED, result.getStatus());
        assertTrue(solutionRepository.existsById(1L));
    }

    @Test
    void not_accepted_solution_attempt() throws JsonProcessingException {
        ExecutionResultDto resultDto = getValidExecutionResultDtoWithInorrectSolution();

        when(codeExecutionService.executeCode(any(ExecutionRequestDto.class)))
                .thenReturn(resultDto);

        var result = solutionService.solutionAttempt(getValidSolutionAttemptDto(), "user");
        assertEquals(SolutionResult.NOT_ACCEPTED, result.getStatus());
        assertFalse(solutionRepository.existsById(2L));
    }

    @Test
    void get_all_solutions_for_task_with_solving() throws AccessException {
        var result = solutionService.getAllSolutionsForTask(1L, "admin");
        assertFalse(result.isEmpty());
    }

    @Test
    void get_all_solutions_for_task_without_solving() {
        assertThrows(AccessException.class, () -> solutionService.getAllSolutionsForTask(1L, "user"));
    }

    private ExecutionResultDto getValidExecutionResultDtoWithCorrectSolution() {
        ExecutionResultDto executionResultDto = new ExecutionResultDto();
        executionResultDto.setStdin("123");
        executionResultDto.setStdout("321");
        executionResultDto.setStatus("success");
        executionResultDto.setExecutionTime(1);
        return executionResultDto;
    }

    private ExecutionResultDto getValidExecutionResultDtoWithInorrectSolution() {
        ExecutionResultDto executionResultDto = new ExecutionResultDto();
        executionResultDto.setStdin("some stdin");
        executionResultDto.setStdout("INCORRECT STOUT");
        executionResultDto.setStatus("success");
        executionResultDto.setExecutionTime(1);
        return executionResultDto;
    }

    private SolutionAttemptDto getValidSolutionAttemptDto() {
        SolutionAttemptDto solutionAttemptDto = new SolutionAttemptDto();
        solutionAttemptDto.setTaskId(1L);
        solutionAttemptDto.setLanguage("java");
        solutionAttemptDto.setCode("code");
        return solutionAttemptDto;
    }
}