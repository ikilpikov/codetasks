package ru.sber.codetasks.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.sber.codetasks.dto.auth.LoginRequest;
import ru.sber.codetasks.dto.auth.RegisterRequest;
import ru.sber.codetasks.service.UserService;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserService userService;

    public static final String FIELDS_INVALID_MESSAGE = "Fields are invalid";

    public static final String CANNOT_AUTHENTICATE = "Cannot authenticate";

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public Map<String, String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public Map<String, String> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<String> jwtExceptionHandler(Exception ex) {
        return new ResponseEntity<>(CANNOT_AUTHENTICATE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> invalidDataExceptionHandler(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(FIELDS_INVALID_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(KeyAlreadyExistsException.class)
    public ResponseEntity<String> keyAlreadyExistsExceptionHandler(KeyAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
