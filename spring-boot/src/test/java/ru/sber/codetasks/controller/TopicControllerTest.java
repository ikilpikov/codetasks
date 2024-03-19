package ru.sber.codetasks.controller;

import annotation.WithMockAdmin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.sber.codetasks.dto.topic.ReducedTopicDto;
import ru.sber.codetasks.dto.topic.TopicDto;
import ru.sber.codetasks.service.TopicService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TopicController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TopicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TopicService topicService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void add_valid_topic() throws Exception {
        doNothing().when(topicService).createTopic(any(ReducedTopicDto.class));

        ReducedTopicDto topicDto = getValidReducedTopicDto();
        String jsonContent = objectMapper.writeValueAsString(topicDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/topic/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Topic added successfully"));
    }

    @Test
    @WithMockAdmin
    void add_invalid_topic() throws Exception {
        doNothing().when(topicService).createTopic(any(ReducedTopicDto.class));

        String jsonContent = objectMapper.writeValueAsString(new ReducedTopicDto(""));

        mockMvc.perform(MockMvcRequestBuilders.post("/topic/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockAdmin
    void remove_existing_topic() throws Exception {
        doNothing().when(topicService).deleteTopic(anyLong());
        Long topicId = 1L;

        mockMvc.perform(delete("/topic/delete/{id}", topicId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Topic deleted successfully"));
    }

    @Test
    @WithMockAdmin
    void remove_not_existing_topic() throws Exception {
        doThrow(EntityNotFoundException.class).when(topicService).deleteTopic(anyLong());
        Long topicId = 1L;

        mockMvc.perform(delete("/topic/delete/{id}", topicId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockAdmin
    void remove_constraint_violation_topic() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(topicService).deleteTopic(anyLong());
        Long topicId = 1L;

        mockMvc.perform(delete("/topic/delete/{id}", topicId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockAdmin
    void get_existing_topic() throws Exception {
        ReducedTopicDto reducedTopicDto = getValidReducedTopicDto();
        when(topicService.getTopic(anyLong())).thenReturn(reducedTopicDto);

        Long topicId = 1L;

        mockMvc.perform(get("/topic/{id}", topicId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    @WithMockUser
    void get_not_existing_topic() throws Exception {
        doThrow(EntityNotFoundException.class).when(topicService).getTopic(anyLong());
        Long topicId = 1L;

        mockMvc.perform(get("/topic/{id}", topicId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void get_all_topics() throws Exception {
        when(topicService.getTopics())
                .thenReturn(List.of(getValidTopicDto()));

        mockMvc.perform(get("/topic/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    @WithMockAdmin
    void update_existing_topic() throws Exception {
        doNothing().when(topicService)
                .updateTopic(any(Long.class), any(ReducedTopicDto.class));

        ReducedTopicDto topicDto = getValidReducedTopicDto();

        String jsonContent = objectMapper.writeValueAsString(topicDto);
        Long topicId = 1L;

        mockMvc.perform(put("/topic/update/{id}", topicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Topic updated successfully"));
    }

    @Test
    @WithMockAdmin
    void update_not_existing_topic() throws Exception {
        doThrow(EntityNotFoundException.class)
                .when(topicService).updateTopic(any(Long.class), any(ReducedTopicDto.class));
        ReducedTopicDto topicDto = getValidReducedTopicDto();

        String jsonContent = objectMapper.writeValueAsString(topicDto);

        Long topicId = 1L;

        mockMvc.perform(put("/topic/update/{id}", topicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound());
    }

    private ReducedTopicDto getValidReducedTopicDto() {
        return new ReducedTopicDto("name");
    }

    private TopicDto getValidTopicDto() {
        return new TopicDto(1L, "name");
    }

}