package ru.sber.codetasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.expression.AccessException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.sber.codetasks.dto.code_execution.ExecutionRequestDto;
import ru.sber.codetasks.dto.code_execution.ExecutionResultDto;
import ru.sber.codetasks.dto.solution.SolutionAttemptDto;
import ru.sber.codetasks.dto.solution.SolutionResponseDto;
import ru.sber.codetasks.dto.solution.SolutionUserDto;
import ru.sber.codetasks.enums.SolutionResult;
import ru.sber.codetasks.service.SolutionService;

import java.sql.Timestamp;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolutionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class SolutionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SolutionService solutionService;

    @Test
    @WithMockUser
    void execute_valid_code_without_checks() throws Exception {
        ExecutionResultDto executionResultDto = getValidExecutionResultDto();
        when(solutionService.executeSolutionCode(any(ExecutionRequestDto.class)))
                .thenReturn(executionResultDto);

        String jsonContent = objectMapper.writeValueAsString(getValidExecutionRequestDto());

        mockMvc.perform(post("/solution/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @WithMockUser
    void execute_invalid_code_without_checks() throws Exception {
        String jsonContent = objectMapper.writeValueAsString(new ExecutionRequestDto());

        mockMvc.perform(post("/solution/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void make_solution_attempt() throws Exception {
        SolutionResponseDto solutionResponseDto = getValidSolutionResponseDto();
        when(solutionService.solutionAttempt(any(SolutionAttemptDto.class), anyString()))
                .thenReturn(solutionResponseDto);

        String jsonContent = objectMapper.writeValueAsString(getValidSolutionAttemptDto());

        mockMvc.perform(post("/solution/attempt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(SolutionResult.ACCEPTED.toString()));
    }

    @Test
    @WithMockUser
    void make_solution_attempt_with_invalid_solution() throws Exception {
        String jsonContent = objectMapper.writeValueAsString(new SolutionAttemptDto());

        mockMvc.perform(post("/solution/attempt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void get_all_solutions_for_task() throws Exception {
        SolutionUserDto solutionUserDto = getValidSolutionUserDto();
        when(solutionService.getAllSolutionsForTask(anyLong(), anyString()))
                .thenReturn(Collections.singletonList(solutionUserDto));
        Long taskId = 1L;

        mockMvc.perform(get("/solution/all/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$[0].id").value("1"));

    }

    @Test
    @WithMockUser
    void get_all_solutions_for_task_without_solving_task() throws Exception {
        when(solutionService.getAllSolutionsForTask(anyLong(), anyString()))
                .thenThrow(AccessException.class);

        Long taskId = 1L;
        mockMvc.perform(get("/solution/all/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private ExecutionResultDto getValidExecutionResultDto() {
        ExecutionResultDto executionResultDto = new ExecutionResultDto();
        executionResultDto.setExecutionTime(1);
        executionResultDto.setStatus("success");
        executionResultDto.setStdin("123");
        executionResultDto.setStdout("321");
        return executionResultDto;
    }

    private ExecutionRequestDto getValidExecutionRequestDto() {
        ExecutionRequestDto executionRequestDto = new ExecutionRequestDto();
        executionRequestDto.setLanguage("python");
        executionRequestDto.setCode("code");
        executionRequestDto.setStdin("123");
        return executionRequestDto;
    }

    private SolutionAttemptDto getValidSolutionAttemptDto() {
        SolutionAttemptDto solutionAttemptDto = new SolutionAttemptDto();
        solutionAttemptDto.setCode("code");
        solutionAttemptDto.setLanguage("python");
        solutionAttemptDto.setTaskId(1L);
        return solutionAttemptDto;
    }

    private SolutionResponseDto getValidSolutionResponseDto() {
        SolutionResponseDto solutionResponseDto = new SolutionResponseDto();
        solutionResponseDto.setTime(1);
        solutionResponseDto.setStatus(SolutionResult.ACCEPTED);
        return solutionResponseDto;
    }

    private SolutionUserDto getValidSolutionUserDto() {
        SolutionUserDto solutionUserDto = new SolutionUserDto();
        solutionUserDto.setSubmissionTime(22);
        solutionUserDto.setSubmissionDate(new Timestamp(1));
        solutionUserDto.setId(1L);
        return solutionUserDto;
    }

}