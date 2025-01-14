package dev.jlipka.pickly.db;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;


@Data
@Builder
public class Message {
    @Builder.Default
    private UUID messageID = UUID.randomUUID();
    @NonNull
    private UUID chatID;
    @NonNull
    private UUID senderID;
    private String messageText;
    @Builder.Default
    private Timestamp sentAt = new Timestamp(System.currentTimeMillis());
    private Set<Attachment> attachments;
}
