package dev.jlipka.pickly.db;

import dev.jlipka.pickly.controller.components.media.MimeType;
import lombok.extern.slf4j.Slf4j;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Slf4j
public class AttachmentMapper {
    public static Attachment mapFromResultSet(ResultSet rs) throws SQLException {
        try {
            return Attachment.builder()
                    .attachmentID((UUID) rs.getObject("attachmentID"))
                    .chatID((UUID) rs.getObject("chatID"))
                    .messageID((UUID) rs.getObject("messageID"))
                    .mimeType(MimeType.valueOf(rs.getString("mimeType")))
                    .attachmentContent(rs.getBlob("attachmentContent"))
                    .uploadedAt(rs.getTimestamp("uploadedAt"))
                    .build();
        } catch (SQLException e) {
            log.error("Failed to map Attachment from ResultSet");
            throw new SQLException(e.getSQLState());
        }
    }
    public static Attachment mapToNewAttachment(UUID chatID, UUID messageID, MimeType mimeType, Blob content) {
        return Attachment.builder()
                .chatID(chatID)
                .messageID(messageID)
                .mimeType(mimeType)
                .attachmentContent(content)
                .build();
    }
}
