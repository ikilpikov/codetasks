package ru.sber.codetasks.controller;

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
import ru.sber.codetasks.dto.programming_language.ProgrammingLanguageDto;
import ru.sber.codetasks.dto.programming_language.ReducedProgrammingLanguageDto;
import ru.sber.codetasks.service.ProgrammingLanguageService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProgrammingLanguageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProgrammingLanguageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProgrammingLanguageService programmingLanguageService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void add_valid_language() throws Exception {
        doNothing().when(programmingLanguageService)
                .createProgrammingLanguage(any(ReducedProgrammingLanguageDto.class));

        ReducedProgrammingLanguageDto languageDto = getValidReducedProgrammingLanguageDto();
        String jsonContent = objectMapper.writeValueAsString(languageDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/programming-language/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Language added successfully"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void add_invalid_language() throws Exception {
        doNothing().when(programmingLanguageService)
                .createProgrammingLanguage(any(ReducedProgrammingLanguageDto.class));

        String jsonContent = objectMapper.writeValueAsString(new ReducedProgrammingLanguageDto(""));

        mockMvc.perform(MockMvcRequestBuilders.post("/programming-language/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void remove_existing_language() throws Exception {
        doNothing().when(programmingLanguageService).deleteProgrammingLanguage(anyLong());
        Long languageId = 1L;

        mockMvc.perform(delete("/programming-language/delete/{id}", languageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Language deleted successfully"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void remove_not_existing_language() throws Exception {
        doThrow(EntityNotFoundException.class).when(programmingLanguageService)
                .deleteProgrammingLanguage(anyLong());
        Long languageId = 1L;

        mockMvc.perform(delete("/programming-language/delete/{id}", languageId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void remove_constraint_violation_language() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(programmingLanguageService)
                .deleteProgrammingLanguage(anyLong());
        Long languageId = 1L;

        mockMvc.perform(delete("/programming-language/delete/{id}", languageId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void get_existing_language() throws Exception {
        ReducedProgrammingLanguageDto reducedLanguageDto = getValidReducedProgrammingLanguageDto();
        when(programmingLanguageService.getProgrammingLanguage(anyLong())).thenReturn(reducedLanguageDto);

        Long languageId = 1L;

        mockMvc.perform(get("/programming-language/{id}", languageId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    @WithMockUser
    void get_not_existing_language() throws Exception {
        doThrow(EntityNotFoundException.class).when(programmingLanguageService)
                .getProgrammingLanguage(anyLong());
        Long languageId = 1L;

        mockMvc.perform(get("/programming-language/{id}", languageId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void get_all_languages() throws Exception {
        when(programmingLanguageService.getProgrammingLanguages())
                .thenReturn(List.of(getValidProgrammingLanguageDto()));

        mockMvc.perform(get("/programming-language/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void update_existing_language() throws Exception {
        doNothing().when(programmingLanguageService)
                .updateProgrammingLanguage(any(Long.class), any(ReducedProgrammingLanguageDto.class));

        ReducedProgrammingLanguageDto languageDto = getValidReducedProgrammingLanguageDto();

        String jsonContent = objectMapper.writeValueAsString(languageDto);
        Long languageId = 1L;

        mockMvc.perform(put("/programming-language/update/{id}", languageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Language updated successfully"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void update_not_existing_language() throws Exception {
        doThrow(EntityNotFoundException.class)
                .when(programmingLanguageService)
                .updateProgrammingLanguage(any(Long.class), any(ReducedProgrammingLanguageDto.class));
        ReducedProgrammingLanguageDto languageDto = getValidReducedProgrammingLanguageDto();

        String jsonContent = objectMapper.writeValueAsString(languageDto);

        Long languageId = 1L;

        mockMvc.perform(put("/language/update/{id}", languageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound());
    }

    private ReducedProgrammingLanguageDto getValidReducedProgrammingLanguageDto() {
        return new ReducedProgrammingLanguageDto("name");
    }

    private ProgrammingLanguageDto getValidProgrammingLanguageDto() {
        return new ProgrammingLanguageDto(1L, "name");
    }

}