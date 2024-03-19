package ru.sber.codetasks.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.sber.codetasks.dto.auth.LoginRequestDto;
import ru.sber.codetasks.dto.auth.RegisterRequestDto;
import ru.sber.codetasks.service.UserService;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.validation.Valid;
import java.util.Map;

import static ru.sber.codetasks.controller.constants.Messages.FIELDS_INVALID_MESSAGE;
import static ru.sber.codetasks.controller.constants.Messages.CANNOT_AUTHENTICATE;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public Map<String, String> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return userService.registerUser(registerRequestDto);
    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public Map<String, String> authenticateUser(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.authenticateUser(loginRequestDto);
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
