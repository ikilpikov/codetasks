package ru.sber.codetasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
class TopicControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    TopicService topicService;

    @Test
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
    void add_invalid_topic() throws Exception {
        doNothing().when(topicService).createTopic(any(ReducedTopicDto.class));

        String jsonContent = objectMapper.writeValueAsString(new ReducedTopicDto(""));

        mockMvc.perform(MockMvcRequestBuilders.post("/topic/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    void remove_existing_topic() throws Exception {
        doNothing().when(topicService).deleteTopic(anyLong());
        Long topicId = 1L;

        mockMvc.perform(delete("/topic/delete/{id}", topicId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Topic deleted successfully"));
    }

    @Test
    void remove_not_existing_topic() throws Exception {
        doThrow(EntityNotFoundException.class).when(topicService).deleteTopic(anyLong());
        Long topicId = 1L;

        mockMvc.perform(delete("/topic/delete/{id}", topicId))
                .andExpect(status().isNotFound());
    }

    @Test
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
    void get_not_existing_topic() throws Exception {
        doThrow(EntityNotFoundException.class).when(topicService).getTopic(anyLong());
        Long topicId = 1L;

        mockMvc.perform(get("/topic/{id}", topicId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTopics() throws Exception {
        when(topicService.getTopics())
                .thenReturn(List.of(getValidTopicDto()));

        mockMvc.perform(get("/topic/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
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

    ReducedTopicDto getValidReducedTopicDto() {
        return new ReducedTopicDto("name");
    }

    TopicDto getValidTopicDto() {
        return new TopicDto(1L, "name");
    }

}