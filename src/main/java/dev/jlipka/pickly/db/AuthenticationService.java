package dev.jlipka.pickly.db;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.UUID;

@Slf4j
public class AuthenticationService {
    private final Connection connection;

    public AuthenticationService(Connection connection) {
        this.connection = connection;
    }

    public boolean checkCredentials(String login, String password) {
        String sql = "SELECT passwordHash FROM userAccounts WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedHash = resultSet.getString("hashedPassword");
                return BCrypt.checkpw(password, storedHash);
            }
            return false;
        } catch (SQLException e) {
            log.error("Failed to check credentials for login: {}", login, e);
            throw new RuntimeException("Authentication failed", e);
        }
    }

    public void registerUser(String login, String password, String username) {
        String sql = "INSERT INTO userAccounts (userID, login, passwordHash, username, status, createdAt) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, UUID.randomUUID());
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, hashPassword(password));
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, "ONLINE");
            preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to register user with login: {}", login, e);
            throw new RuntimeException("User registration failed", e);
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}