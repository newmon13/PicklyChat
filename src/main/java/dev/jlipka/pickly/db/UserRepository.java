package dev.jlipka.pickly.db;

import dev.jlipka.pickly.model.Status;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class UserRepository {
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public Optional<User> findByLogin(String login) {
        String sql = "SELECT * FROM userAccounts WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(UserMapper.mapFromResultSet(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error("Failed to fetch user with login: {}, SQL state: {}", login, e.getSQLState());
            throw new RuntimeException("Failed to fetch user by login", e);
        }
    }


    public Optional<User> findByID(UUID userID) {
        String sql = "SELECT * FROM userAccounts WHERE userID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(UserMapper.mapFromResultSet(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error("Failed to fetch user with userID: {}", userID, e);
            throw new RuntimeException("Failed to fetch user by id", e);
        }
    }
    public void save(User userToSave) {
        String sql = "INSERT INTO userAccounts VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
          ps.setObject(1, userToSave.getUserID());
          ps.setString(2, userToSave.getLogin());
          ps.setString(3, userToSave.getPasswordHash());
          ps.setString(4, userToSave.getUsername());
          ps.setString(5, userToSave.getName());
          ps.setString(6, userToSave.getSurname());
          ps.setString(7, userToSave.getStatus().toString());
          ps.setBlob(8, userToSave.getAvatar());
          ps.setInt(9, userToSave.getAge());
          ps.setTimestamp(10, userToSave.getCreatedAt());
          ps.executeUpdate();
          log.info("Successfully inserted user: {}", userToSave.getUserID());
        } catch (SQLException e) {
            log.error("Failed to insert user: {}", userToSave.getUserID(), e);
            throw new RuntimeException(e);
        }
    }

    public void updateStatusByID(UUID userID, Status status) {
        String sql = "UPDATE userAccounts SET status = ? WHERE userID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.toString());
            ps.setObject(2, userID);
            ps.executeUpdate();
            log.info("Successfully updated user's status to {}", status);
        } catch (SQLException e) {
            log.error("Failed to user's status: {}", userID, e);
            throw new RuntimeException(e);
        }
    }
}
