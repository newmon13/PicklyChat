package dev.jlipka.pickly;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class Message implements Serializable {
    private String senderId;
    private String receiverId;
    private String messageId;
    private String groupId;
    private String message;
    private List<SerializableFile> files;
    private String replyToMessageId;

    private Message(MessageBuilder messageBuilder) {
        this.senderId = messageBuilder.senderId;
        this.receiverId = messageBuilder.receiverId;
        this.messageId = messageBuilder.messageId;
        this.groupId = messageBuilder.groupId;
        this.message = messageBuilder.message;
        this.replyToMessageId = messageBuilder.replyToMessageId;
        this.files = messageBuilder.files;
    }

    public static class MessageBuilder {
        private final String senderId;
        private final String receiverId;
        private final String messageId;
        private String groupId;
        private String message;
        private String replyToMessageId;
        private List<SerializableFile> files;

        public MessageBuilder(String senderId, String receiverId) {
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.messageId = UUID.randomUUID().toString();
        }

        public MessageBuilder addGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public MessageBuilder addMessageContent(String message) {
            this.message = message;
            return this;
        }

        public MessageBuilder replayToMessage(String messageId) {
            this.replyToMessageId = messageId;
            return this;
        }

        public MessageBuilder addSerializableFiles(List<SerializableFile> files) {
            this.files = files;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
