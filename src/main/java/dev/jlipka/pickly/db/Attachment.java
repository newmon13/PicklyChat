package dev.jlipka.pickly.db;

import dev.jlipka.pickly.controller.components.media.MimeType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
public class Attachment {
    @Builder.Default
    private final UUID attachmentID = UUID.randomUUID();
    @NonNull
    private final UUID chatID;
    @NonNull
    private final UUID messageID;
    @NonNull
    private final MimeType mimeType;
    @NonNull
    private final Blob attachmentContent;
    @Builder.Default
    private final Timestamp uploadedAt = new Timestamp(System.currentTimeMillis());
}
