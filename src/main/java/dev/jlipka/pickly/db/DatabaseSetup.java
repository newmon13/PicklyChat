package dev.jlipka.pickly.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    private final Connection connection;

    public DatabaseSetup(Connection connection) {
        this.connection = connection;
    }

    public void createTables() {
        try (Statement statement = connection.createStatement()) {
            // Create chats table first since it's referenced by other tables
            statement.execute("""
                CREATE TABLE chats(
                    chatID UUID PRIMARY KEY,
                    createdAt TIMESTAMP NOT NULL
                )
            """);

            statement.execute("""
                CREATE TABLE userAccounts(
                    userID UUID PRIMARY KEY,
                    login VARCHAR(255) NOT NULL UNIQUE,
                    passwordHash VARCHAR(255) NOT NULL,
                    username VARCHAR(255) NOT NULL UNIQUE,
                    name VARCHAR(255),
                    surname VARCHAR(255),
                    status VARCHAR(20) NOT NULL CHECK (status IN ('ONLINE', 'OFFLINE', 'AWAY', 'BUSY', 'DO_NOT_DISTURB', 'INVISIBLE')),
                    avatar BLOB,
                    age INT,
                    createdAt TIMESTAMP NOT NULL
                )
            """);

            statement.execute("""
                CREATE TABLE messages(
                    messageID UUID PRIMARY KEY,
                    chatID UUID NOT NULL,
                    senderID UUID NOT NULL,
                    messageText VARCHAR(4000),
                    sentAt TIMESTAMP NOT NULL,
                    FOREIGN KEY (chatID) REFERENCES chats(chatID),
                    FOREIGN KEY (senderID) REFERENCES userAccounts(userID)
                )
            """);

            statement.execute("""
                CREATE TABLE chatParticipants(
                    chatID UUID NOT NULL,
                    userID UUID NOT NULL,
                    PRIMARY KEY (chatID, userID),
                    FOREIGN KEY (chatID) REFERENCES chats(chatID),
                    FOREIGN KEY (userID) REFERENCES userAccounts(userID)
                )
            """);

            statement.execute("""
                CREATE TABLE attachments(
                    attachmentID UUID PRIMARY KEY,
                    messageID UUID NOT NULL,
                    mimeType VARCHAR(20) CHECK (mimeType IN ('ARCHIVE', 'PDF', 'TEXT', 'IMAGE', 'AUDIO', 'VIDEO', 'UNKNOWN')),
                    attachmentContent BLOB NOT NULL,
                    uploadedAt TIMESTAMP NOT NULL,
                    FOREIGN KEY (messageID) REFERENCES messages(messageID)
                )
            """);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database tables", e);
        }
    }
}