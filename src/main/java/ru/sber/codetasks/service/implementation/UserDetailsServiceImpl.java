package ru.sber.codetasks.service.implementation;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sber.codetasks.repository.UserRepository;
import ru.sber.codetasks.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var person = userRepository.findByUsername(username);
        return person
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("user " + username + " not found"));
    }

}
