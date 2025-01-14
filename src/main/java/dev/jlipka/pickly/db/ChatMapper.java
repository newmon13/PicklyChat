package dev.jlipka.pickly.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class ChatMapper {
    public static Chat mapFromResultSet(ResultSet rs) throws SQLException {
        try {
            return Chat.builder()
                    .chatID((UUID) rs.getObject("chatID"))
                    .createdAt(rs.getTimestamp("createdAt"))
                    .build();
        } catch (SQLException e) {
            log.error("Failed to map Chat from ResultSet");
            throw new SQLException(e.getSQLState());
        }
    }

    public static Set<User> getChatParticipants(UUID chatID, Connection connection) throws SQLException {
        Set<User> participants = new HashSet<>();
        String query = """
            SELECT u.*
            FROM userAccounts u
            JOIN chatParticipants cp ON u.userID = cp.userID
            WHERE cp.chatID = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, chatID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                participants.add(UserMapper.mapFromResultSet(rs));
            }
        }
        return participants;
    }

    public static Set<Message> getChatMessages(UUID chatID, Connection connection) throws SQLException {
        Set<Message> messages = new HashSet<>();
        String query = "SELECT * FROM messages WHERE chatID = ? ORDER BY sentAt";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, chatID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(MessageMapper.mapFromResultSet(rs));
            }
        }
        return messages;
    }

    public static Set<Attachment> getChatAttachments(UUID chatID, Connection connection) throws SQLException {
        Set<Attachment> attachments = new HashSet<>();
        String query = "SELECT * FROM attachments WHERE chatID = ? ORDER BY uploadedAt";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, chatID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attachments.add(AttachmentMapper.mapFromResultSet(rs));
            }
        }
        return attachments;
    }

    public static Chat mapToNewChat(Set<UUID> participantIDs, Connection connection) throws SQLException {
        Chat chat = Chat.builder().build();

        String insertParticipant = "INSERT INTO chatParticipants (chatID, userID) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertParticipant)) {
            for (UUID userID : participantIDs) {
                stmt.setObject(1, chat.getChatID());
                stmt.setObject(2, userID);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }

        return chat;
    }

    public static Chat getFullChat(UUID chatID, Connection connection) throws SQLException {
        return Chat.builder()
                .chatID(chatID)
                .chatParticipants(getChatParticipants(chatID, connection))
                .chatMessages(getChatMessages(chatID, connection))
                .chatAttachments(getChatAttachments(chatID, connection))
                .build();
    }
}