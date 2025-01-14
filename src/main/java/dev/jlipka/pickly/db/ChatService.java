package dev.jlipka.pickly.db;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Builder
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final AttachmentRepository attachmentRepository;

    public Chat createChat(UUID creatorUserID, Set<UUID> participantIDs) {
        log.info("Creating new chat with {} participants", participantIDs.size());
        for (UUID userID : participantIDs) {
            if (userRepository.findByID(userID).isEmpty()) {
                log.error("User not found: {}", userID);
                throw new IllegalArgumentException("User not found: " + userID);
            }
        }

        Set<User> participants = new HashSet<>();
        for (UUID userID : participantIDs) {
            userRepository.findByID(userID).ifPresent(participants::add);
        }

        Chat newChat = Chat.builder()
                .chatParticipants(participants)
                .build();

        chatRepository.save(newChat);
        log.info("Chat created successfully with ID: {}", newChat.getChatID());
        return newChat;
    }

    public void sendMessage(Message message) {
        log.info("Sending message in chat: {} from user: {}", message.getChatID(), message.getSenderID());
        Optional<Chat> chatOpt = chatRepository.findById(message.getChatID());
        if (chatOpt.isEmpty()) {
            log.error("Chat not found: {}", message.getChatID());
            throw new IllegalArgumentException("Chat not found: " + message.getChatID());
        }
        messageRepository.save(message);
        handleAttachments(message);
        log.info("Message sent successfully in chat: {}", message.getChatID());
    }

    private void handleAttachments(Message message) {
        if (attachmentsNotEmpty(message.getAttachments())) {
            for (Attachment attachment: message.getAttachments()) {
                attachmentRepository.save(attachment);
                log.info("Attachment sent successfully in chat: {}", attachment.getAttachmentID());
            }
        }
    }

    private boolean attachmentsNotEmpty(Set<Attachment> attachments) {
        return Objects.nonNull(attachments) && !attachments.isEmpty();
    }
    
    public List<Chat> getUserChats(UUID userID) {
        return chatRepository.findAllByParticipantID(userID);
    }
    
    public Optional<Chat> getChat(UUID chatID) {
        return chatRepository.findById(chatID);
    }
    
    public void deleteChat(UUID chatID, UUID requesterID) {

        try {
            Optional<Chat> chatOpt = chatRepository.findById(chatID);
            if (chatOpt.isEmpty()) {
                throw new IllegalArgumentException("Chat not found: " + chatID);
            }
            
            Chat chat = chatOpt.get();
            boolean isRequesterParticipant = chat.getChatParticipants().stream()
                    .anyMatch(user -> user.getUserID().equals(requesterID));
                    
            if (!isRequesterParticipant) {
                throw new IllegalArgumentException("User is not authorized to delete this chat: " + requesterID);
            }
            
            chatRepository.delete(chatID);
            
        } catch (Exception e) {
            log.error("Failed to delete chat", e);
            throw new RuntimeException("Failed to delete chat", e);
        }
    }
}