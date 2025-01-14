package dev.jlipka.pickly.db;


import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class ChatRepository {
    private final Connection connection;

    public ChatRepository(Connection connection) {
        this.connection = connection;
    }

    public Set<Attachment> getChatAttachments(UUID chatID) {
        String attachmentsQuery = "SELECT * FROM attachments WHERE chatID = ?";
        Set<Attachment> attachments = new HashSet<>();

        try (PreparedStatement preStatement = connection.prepareStatement(attachmentsQuery)) {
            preStatement.setObject(1, chatID);
            ResultSet resultSet = preStatement.executeQuery();

            while (resultSet.next()) {
                attachments.add(AttachmentMapper.mapFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.error("Failed to fetch attachments for chatID: {}", chatID, e);
            throw new RuntimeException("Failed to fetch attachments", e);
        }

        return attachments;
    }

    public void save(Chat chat) {
        try {
            connection.setAutoCommit(false);
            try {
                String chatQuery = "INSERT INTO chats (chatID, createdAt) VALUES (?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(chatQuery)) {
                    stmt.setObject(1, chat.getChatID());
                    stmt.setTimestamp(2, chat.getCreatedAt());
                    stmt.executeUpdate();
                    log.info("Successfully inserted chat {}", chat.getChatID());
                }

                String participantQuery = "INSERT INTO chatParticipants (chatID, userID) VALUES (?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(participantQuery)) {
                    for (User participant : chat.getChatParticipants()) {
                        stmt.setObject(1, chat.getChatID());
                        stmt.setObject(2, participant.getUserID());
                        stmt.executeUpdate();
                        log.info("Successfully inserted chat participant {}", participant.getUserID());
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error("Failed to save chat: {}", chat.getChatID(), e);
            throw new RuntimeException("Failed to save chat", e);
        }
    }

    public Optional<Chat> findById(UUID chatID) {
        try {
            String query = "SELECT * FROM chats WHERE chatID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setObject(1, chatID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Chat chat = ChatMapper.mapFromResultSet(rs);
                    chat.setChatParticipants(ChatMapper.getChatParticipants(chatID, connection));
                    chat.setChatMessages(ChatMapper.getChatMessages(chatID, connection));
                    chat.setChatAttachments(ChatMapper.getChatAttachments(chatID, connection));
                    return Optional.of(chat);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Failed to find chat by ID: {}", chatID, e);
            throw new RuntimeException("Failed to find chat by ID", e);
        }
    }

    public void delete(UUID chatID) {
        try {
            connection.setAutoCommit(false);
            try {
                String attachmentsQuery = "DELETE FROM attachments WHERE chatID = ?";
                try (PreparedStatement stmt = connection.prepareStatement(attachmentsQuery)) {
                    stmt.setObject(1, chatID);
                    stmt.executeUpdate();
                }

                String messagesQuery = "DELETE FROM messages WHERE chatID = ?";
                try (PreparedStatement stmt = connection.prepareStatement(messagesQuery)) {
                    stmt.setObject(1, chatID);
                    stmt.executeUpdate();
                }

                String participantsQuery = "DELETE FROM chatParticipants WHERE chatID = ?";
                try (PreparedStatement stmt = connection.prepareStatement(participantsQuery)) {
                    stmt.setObject(1, chatID);
                    stmt.executeUpdate();
                }

                String chatQuery = "DELETE FROM chats WHERE chatID = ?";
                try (PreparedStatement stmt = connection.prepareStatement(chatQuery)) {
                    stmt.setObject(1, chatID);
                    stmt.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error("Failed to delete chat: {}", chatID, e);
            throw new RuntimeException("Failed to delete chat", e);
        }
    }

    public List<Chat> findAllByParticipantID(UUID userID) {
        String query = """
            SELECT c.*
            FROM chats c
            JOIN chatParticipants cp ON c.chatID = cp.chatID
            WHERE cp.userID = ?
            ORDER BY c.createdAt DESC
        """;

        try {
            List<Chat> chats = new ArrayList<>();

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setObject(1, userID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Chat chat = ChatMapper.mapFromResultSet(rs);
                    chat.setChatParticipants(ChatMapper.getChatParticipants(chat.getChatID(), connection));
                    chat.setChatMessages(ChatMapper.getChatMessages(chat.getChatID(), connection));
                    chat.setChatAttachments(ChatMapper.getChatAttachments(chat.getChatID(), connection));
                    chats.add(chat);
                }
            }

            return chats;
        } catch (SQLException e) {
            log.error("Failed to find chats for user: {}", userID, e);
            throw new RuntimeException("Failed to find chats by participant ID", e);
        }
    }
}