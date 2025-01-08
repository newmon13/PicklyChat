package dev.jlipka.pickly.db;

import dev.jlipka.pickly.model.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserRepository {
    private final Connection connection;
    private final UserMapper userMapper;

    public UserRepository(Connection connection) {
        this.connection = connection;
        this.userMapper = new UserMapper();
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM userAccounts";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(userMapper.mapFromResultSet(resultSet));
            }
            return users;
        } catch (SQLException e) {
            log.error("Failed to fetch all users", e);
            throw new RuntimeException("Failed to fetch users", e);
        }
    }

    public Optional<User> getUserByUsername(String username) {
        String sql = "SELECT * FROM userAccounts WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(userMapper.mapFromResultSet(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Failed to fetch user by username: {}", username, e);
            throw new RuntimeException("Failed to fetch user", e);
        }
    }

    public synchronized void updateUser(User user) {
        String sql = "UPDATE userAccounts SET name = ?, surname = ?, status = ?, avatar = ?, age = ? WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getStatus().toString());
            preparedStatement.setBytes(4, user.getAvatar());
            preparedStatement.setInt(5, user.getAge());
            preparedStatement.setString(6, user.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to update user: {}", user.getUsername(), e);
            throw new RuntimeException("Failed to update user", e);
        }
    }
}