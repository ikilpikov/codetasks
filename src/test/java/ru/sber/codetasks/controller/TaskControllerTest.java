package ru.sber.codetasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.sber.codetasks.domain.enums.Difficulty;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.task.ReducedTaskDto;
import ru.sber.codetasks.dto.task.TaskUserDto;
import ru.sber.codetasks.dto.testcase.NewTestCaseDto;
import ru.sber.codetasks.service.TaskService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void add_valid_task() throws Exception {
        doNothing().when(taskService).createTask(any(CreateUpdateTaskDto.class));

        CreateUpdateTaskDto taskDto = getValidCreateUpdateTaskDto();

        String jsonContent = objectMapper.writeValueAsString(taskDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/task/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Task added successfully"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void add_invalid_task() throws Exception {
        doNothing().when(taskService).createTask(any(CreateUpdateTaskDto.class));

        String jsonContent = objectMapper.writeValueAsString(new CreateUpdateTaskDto());

        mockMvc.perform(MockMvcRequestBuilders.post("/task/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void remove_existing_task() throws Exception {
        doNothing().when(taskService).deleteTask(anyLong());
        Long taskId = 1L;

        mockMvc.perform(delete("/task/delete/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void remove_not_existing_task() throws Exception {
        doThrow(EntityNotFoundException.class).when(taskService).deleteTask(anyLong());
        Long taskId = 1L;

        mockMvc.perform(delete("/task/delete/{id}", taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void get_existing_task() throws Exception {
        TaskUserDto taskUserDto = getValidTaskUserDto();

        when(taskService.getTask(anyLong(), anyString())).thenReturn(taskUserDto);

        mockMvc.perform(get("/task/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    @WithMockUser
    void get_not_existing_task() throws Exception {
        doThrow(EntityNotFoundException.class).when(taskService).getTask(1L, "user");

        mockMvc.perform(get("/task/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void get_all_tasks() throws Exception {
        when(taskService.getTasks(anyInt(), anyInt(), any(), any(), any()))
                .thenReturn(List.of(getValidReducedTaskDto()));

        mockMvc.perform(get("/task/all")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void update_existing_task() throws Exception {
        doNothing().when(taskService)
                .updateTask(any(Long.class), any(CreateUpdateTaskDto.class));

        CreateUpdateTaskDto taskDto = getValidCreateUpdateTaskDto();

        String jsonContent = objectMapper.writeValueAsString(taskDto);
        Long taskId = 1L;

        mockMvc.perform(put("/task/update/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Task updated successfully"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void update_not_existing_task() throws Exception {
        doThrow(EntityNotFoundException.class)
                .when(taskService).updateTask(any(Long.class), any(CreateUpdateTaskDto.class));
        CreateUpdateTaskDto taskDto = getValidCreateUpdateTaskDto();

        String jsonContent = objectMapper.writeValueAsString(taskDto);

        Long taskId = 1L;

        mockMvc.perform(put("/task/update/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isNotFound());
    }

    private CreateUpdateTaskDto getValidCreateUpdateTaskDto() {
        CreateUpdateTaskDto taskDto = new CreateUpdateTaskDto();
        taskDto.setName("task");
        taskDto.setCondition("condition");
        taskDto.setTopic("topic");
        taskDto.setDifficulty(Difficulty.MEDIUM);
        taskDto.setLanguages(List.of("java", "python"));
        taskDto.setTestcases(List.of(new NewTestCaseDto("123", "123")));
        return taskDto;
    }

    private TaskUserDto getValidTaskUserDto() {
        TaskUserDto taskUserDto = new TaskUserDto();
        taskUserDto.setName("name");
        taskUserDto.setTopic("topic");
        taskUserDto.setDifficulty(Difficulty.EASY);
        taskUserDto.setLanguages(List.of("python"));
        return taskUserDto;
    }

    private ReducedTaskDto getValidReducedTaskDto() {
        return new ReducedTaskDto(1L,
                "name", "topic", Difficulty.EASY, List.of("python", "string"));
    }


}