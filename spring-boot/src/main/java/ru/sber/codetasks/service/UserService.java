package ru.sber.codetasks.service;

import ru.sber.codetasks.dto.auth.LoginRequestDto;
import ru.sber.codetasks.dto.auth.RegisterRequestDto;

import java.util.Map;

public interface UserService {
    Map<String, String> authenticateUser(LoginRequestDto loginRequestDto);

    Map<String, String> registerUser(RegisterRequestDto request);

    boolean userExists(RegisterRequestDto request);

}
