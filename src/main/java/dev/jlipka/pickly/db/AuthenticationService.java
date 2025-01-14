package dev.jlipka.pickly.db;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;


import javax.security.sasl.AuthenticationException;
import java.sql.*;

@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticate(String login, String password) {
        try {
            return userRepository.findByLogin(login)
                    .filter(user -> BCrypt.checkpw(password, user.getPasswordHash()))
                    .orElseThrow(() -> new AuthenticationException("Invalid credentials: " + login));

        } catch (AuthenticationException e) {
            log.error("Failed to authenticate user with login: {}", login, e);
            throw new RuntimeException("Authentication failed", e);
        }
    }
    public User register(String login, String hashedPassword, String username) {
        if (userRepository.findByLogin(login).isPresent()) {
            log.error("Failed to register user - login already taken: {}", login);
            throw new RuntimeException("User with login already exists: " + login);
        }
        User userToSave = UserMapper.mapToNewUser(login, hashedPassword, username);
        userRepository.save(userToSave);
        return userToSave;
    }
}