package ru.sber.codetasks.controller;

import annotation.WithMockAdmin;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.sber.codetasks.domain.enums.Difficulty;
import ru.sber.codetasks.dto.comment.CreateCommentDto;
import ru.sber.codetasks.dto.comment.LikeUnlikeCommentDto;
import ru.sber.codetasks.dto.task.CreateUpdateTaskDto;
import ru.sber.codetasks.dto.task.ReducedTaskDto;
import ru.sber.codetasks.dto.task.TaskUserDto;
import ru.sber.codetasks.dto.testcase.NewTestCaseDto;
import ru.sber.codetasks.exception.CommentAlreadyLikedException;
import ru.sber.codetasks.service.TaskService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    @WithMockAdmin
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
    @WithMockAdmin
    void add_invalid_task() throws Exception {
        doNothing().when(taskService).createTask(any(CreateUpdateTaskDto.class));

        String jsonContent = objectMapper.writeValueAsString(new CreateUpdateTaskDto());

        mockMvc.perform(MockMvcRequestBuilders.post("/task/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockAdmin
    void remove_existing_task() throws Exception {
        doNothing().when(taskService).deleteTask(anyLong());
        Long taskId = 1L;

        mockMvc.perform(delete("/task/delete/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully"));
    }

    @Test
    @WithMockAdmin
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
    @WithMockAdmin
    void get_existing_task_for_update() throws Exception {
        when(taskService.getTaskForUpdating(anyLong())).thenReturn(getValidCreateUpdateTaskDto());

        mockMvc.perform(get("/task/update/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("task"));
    }

    @Test
    @WithMockAdmin
    void get_not_existing_task_for_update() throws Exception {
        when(taskService.getTaskForUpdating(anyLong())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/task/update/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockAdmin
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
    @WithMockAdmin
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

    @Test
    @WithMockUser
    void count_tasks() throws Exception {
        var expectedCount = 22L;
        when(taskService.countTasks()).thenReturn(expectedCount);

        mockMvc.perform(get("/task/count"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedCount)));
    }

    @Test
    @WithMockUser
    void add_valid_comment() throws Exception {
        doNothing().when(taskService).addComment(any(), any(), any());
        var taskId = 1L;
        String jsonContent = objectMapper.writeValueAsString(getValidCreateCommentDto());

        mockMvc.perform(post("/task/{id}/comment/add", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment added successfully"));
    }

    @Test
    @WithMockUser
    void add_invalid_comment() throws Exception {
        doNothing().when(taskService).addComment(any(), any(), any());
        var taskId = 1L;
        String jsonContent = objectMapper.writeValueAsString(new CreateUpdateTaskDto());

        mockMvc.perform(post("/task/{id}/comment/add", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void add_comment_with_not_existing_entity() throws Exception {
        doThrow(EntityNotFoundException.class)
                .when(taskService).addComment(anyLong(), any(CreateCommentDto.class), anyString());
        var taskId = 1L;

        mockMvc.perform(post("/task/{id}/comment/add", taskId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void remove_existing_comment() throws Exception {
        doNothing().when(taskService).deleteComment(anyLong(), anyString());
        Long taskId = 1L;

        mockMvc.perform(delete("/task/comment/delete/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment deleted successfully"));
    }

    @Test
    @WithMockUser
    void remove_not_existing_comment() throws Exception {
        doThrow(EntityNotFoundException.class).when(taskService).deleteComment(anyLong(), anyString());
        Long taskId = 1L;

        mockMvc.perform(delete("/task/comment/delete/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void remove_comment_without_rights() throws Exception {
        doThrow(AccessException.class).when(taskService).deleteComment(anyLong(), anyString());
        Long taskId = 1L;

        mockMvc.perform(delete("/task/comment/delete/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    void like_comment() throws Exception {
        doNothing().when(taskService).likeComment(any(LikeUnlikeCommentDto.class), anyString());

        String jsonContent = objectMapper.writeValueAsString(getValidLikeUnlikeCommentDto());

        mockMvc.perform(post("/task/comment/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment liked successfully"));
    }

    @Test
    @WithMockUser
    void like_already_liked_comment() throws Exception {
        doThrow(CommentAlreadyLikedException.class).when(taskService).likeComment(any(LikeUnlikeCommentDto.class), anyString());

        String jsonContent = objectMapper.writeValueAsString(getValidLikeUnlikeCommentDto());

        mockMvc.perform(post("/task/comment/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void unlike_comment() throws Exception {
        doNothing().when(taskService).unlikeComment(any(LikeUnlikeCommentDto.class), anyString());

        String jsonContent = objectMapper.writeValueAsString(getValidLikeUnlikeCommentDto());

        mockMvc.perform(post("/task/comment/unlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment unliked successfully"));
    }

    @Test
    @WithMockUser
    void unlike_not_liked_comment() throws Exception {
        doThrow(EntityNotFoundException.class).when(taskService).unlikeComment(any(LikeUnlikeCommentDto.class), anyString());

        String jsonContent = objectMapper.writeValueAsString(getValidLikeUnlikeCommentDto());

        mockMvc.perform(post("/task/comment/unlike")
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

    private CreateCommentDto getValidCreateCommentDto() {
        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setText("text");
        return createCommentDto;
    }

    private LikeUnlikeCommentDto getValidLikeUnlikeCommentDto() {
        LikeUnlikeCommentDto likeUnlikeCommentDto = new LikeUnlikeCommentDto();
        likeUnlikeCommentDto.setId(1L);
        return likeUnlikeCommentDto;
    }

}