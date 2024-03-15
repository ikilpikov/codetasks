package ru.sber.codetasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.sber.codetasks.dto.auth.LoginRequest;
import ru.sber.codetasks.dto.auth.RegisterRequest;
import ru.sber.codetasks.service.UserService;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithAnonymousUser
    void register_valid_user() throws Exception {
        when(userService.registerUser(any(RegisterRequest.class)))
                .thenReturn(Map.of("token", "token"));

        RegisterRequest registerRequest = getValidRegisterRequest();
        String jsonContent = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @WithAnonymousUser
    void register_invalid_user() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        String jsonContent = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void register_already_existing_user() throws Exception {
        when(userService.registerUser(any(RegisterRequest.class)))
                .thenThrow(KeyAlreadyExistsException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void authenticate_valid_user() throws Exception {
        when(userService.authenticateUser(any(LoginRequest.class)))
                .thenReturn(Map.of("token", "token"));

        LoginRequest loginRequest = getValidLoginRequest();
        String jsonContent = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    private RegisterRequest getValidRegisterRequest() {
        return new RegisterRequest("user", "pass");
    }

    private LoginRequest getValidLoginRequest() {
        return new LoginRequest("user", "pass");
    }

}