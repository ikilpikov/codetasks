package ru.sber.codetasks.service;

import ru.sber.codetasks.dto.auth.LoginRequest;
import ru.sber.codetasks.dto.auth.RegisterRequest;

import java.util.Map;

public interface UserService {
    Map<String, String> authenticateUser(LoginRequest loginRequest);

    Map<String, String> registerUser(RegisterRequest request);

    boolean userExists(RegisterRequest request);

}
