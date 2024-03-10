package ru.sber.codetasks.service.implementation;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sber.codetasks.domain.User;
import ru.sber.codetasks.repository.UserRepository;
import ru.sber.codetasks.domain.enums.Role;
import ru.sber.codetasks.dto.auth.LoginRequest;
import ru.sber.codetasks.dto.auth.RegisterRequest;
import ru.sber.codetasks.security.TokenProvider;
import ru.sber.codetasks.service.UserService;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder encoder,
                           AuthenticationManager authenticationManager,
                           TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Map<String, String> authenticateUser(LoginRequest loginRequest) {
        return createToken(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @Override
    public Map<String, String> registerUser(RegisterRequest request) {
        if (userExists(request)) {
            throw new KeyAlreadyExistsException("User " + request.getUsername() + " already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setRole(Role.ROLE_USER);

        var encodedPassword = encoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        return createToken(request.getUsername(), request.getPassword());
    }

    @Override
    public boolean userExists(RegisterRequest request) {
        return userRepository.existsByUsername(request.getUsername());
    }

    private Map<String, String> createToken(String username, String password) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                username,
                password
        );

        var authority = authenticationManager
                .authenticate(authInputToken)
                .getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(() -> new SecurityException("Role undefined"));

        String token = tokenProvider.createToken(username, authority);
        return Map.of("token", token);
    }

}
