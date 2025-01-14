package dev.jlipka.pickly.db;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class Chat {
    @Builder.Default
    private UUID chatID = UUID.randomUUID();

    @Builder.ObtainVia(method = "getChatParticipants")
    private Set<User> chatParticipants;
    @Builder.ObtainVia(method = "getChatMessages")
    private Set<Message> chatMessages;
    @Builder.ObtainVia(method = "getChatAttachments")
    private Set<Attachment> chatAttachments;
    @Builder.Default
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

}