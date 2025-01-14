package dev.jlipka.pickly.db;


import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class MessageRepository {
    private final Connection connection;

    public MessageRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Message message) {
        String sql = "INSERT INTO messages VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, message.getMessageID());
            ps.setObject(2, message.getChatID());
            ps.setObject(3, message.getSenderID());
            ps.setString(4, message.getMessageText());
            ps.setTimestamp(5, message.getSentAt());
            ps.executeUpdate();
            log.info("Successfully inserted message: {}", message.getMessageID());
        } catch (SQLException e) {
            log.error("Failed to save message: {}", message.getMessageID(), e);
            throw new RuntimeException(e);
        }
    }
}
