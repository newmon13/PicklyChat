package dev.jlipka.pickly.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class MessageMapper {
    public static Message mapFromResultSet(ResultSet rs) throws SQLException {
            return Message.builder()
                    .messageID((UUID) rs.getObject("messageID"))
                    .chatID((UUID) rs.getObject("chatID"))
                    .senderID((UUID) rs.getObject("senderID"))
                    .messageText(rs.getString("messageText"))
                    .sentAt(rs.getTimestamp("sentAt"))
                    .build();
    }

    public static Message mapToNewMessage(UUID chatID, UUID senderID, String messageText) {
        return Message.builder()
                .chatID(chatID)
                .senderID(senderID)
                .messageText(messageText)
                .build();
    }

    public static Message mapToNewMessageWithAttachments(UUID chatID, UUID senderID, String messageText, Set<Attachment> attachments) {
        return Message.builder()
                .chatID(chatID)
                .senderID(senderID)
                .messageText(messageText)
                .attachments(attachments)
                .build();
    }
}
