package dev.jlipka.pickly.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class AttachmentRepository {
    private final Connection connection;

    public AttachmentRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Attachment attachment) {
        String sql = "INSERT INTO attachments VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, attachment.getAttachmentID());
            ps.setObject(2, attachment.getChatID());
            ps.setObject(3, attachment.getMessageID());
            ps.setString(4, attachment.getMimeType().toString());
            ps.setBlob(5, attachment.getAttachmentContent());
            ps.setTimestamp(6, attachment.getUploadedAt());

            ps.executeQuery();
            log.info("Successfully uploaded attachment: {}", attachment.getAttachmentID());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
