package dev.jlipka.pickly.db;

import dev.jlipka.pickly.model.Status;
import dev.jlipka.pickly.db.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class DbTest {
    public static void main(String[] args) throws SQLException {

        DatabaseConnectionManager.initialize(DatabaseConfig.createDefault());
        Connection connection = DatabaseConnectionManager.getConnection();
        DatabaseSetup databaseSetup = new DatabaseSetup(connection);
        databaseSetup.createTables();

        ChatRepository chatRepository = new ChatRepository(connection);
        UserRepository userRepository = new UserRepository(connection);
        MessageRepository messageRepository = new MessageRepository(connection);
        AttachmentRepository attachmentRepository = new AttachmentRepository(connection);

        AuthenticationService authService = new AuthenticationService(userRepository);
        ChatService chatService = ChatService.builder()
                .chatRepository(chatRepository)
                .userRepository(userRepository)
                .messageRepository(messageRepository)
                .attachmentRepository(attachmentRepository)
                .build();

        try {
            User user1 = authService.register("john_doe", BCrypt.hashpw("password123", BCrypt.gensalt()), "John Doe");
            User user2 = authService.register("jane_doe", BCrypt.hashpw("password456", BCrypt.gensalt()), "Jane Doe");
            User user3 = authService.register("bob_smith", BCrypt.hashpw("password789", BCrypt.gensalt()), "Bob Smith");
            User user4 = authService.register("bobby_smith_junior", BCrypt.hashpw("password432", BCrypt.gensalt()), "Bob Smith Junior");

//            System.out.println("Locking...");
//            Object lock = new Object();
//            synchronized(lock) {
//                try {
//                    lock.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

            System.out.println("Creating chat...");
            Chat chat = chatService.createChat(user1.getUserID(), Set.of(user1.getUserID(), user2.getUserID(), user3.getUserID()));


            System.out.println("Sending messages...");
            Message message1 = MessageMapper.mapToNewMessage(
                    chat.getChatID(),
                    user1.getUserID(),
                    "Hello everyone!"
            );

            Message message2 = MessageMapper.mapToNewMessage(
                    chat.getChatID(),
                    user2.getUserID(),
                    "Hi John!"
            );

            chatService.sendMessage(message1);
            chatService.sendMessage(message2);


            System.out.println("\nTesting queries...");
            Optional<Chat> foundChat = chatRepository.findById(chat.getChatID());
            if (foundChat.isPresent()) {
                System.out.println("Found chat: " + foundChat.get().getChatID());
                System.out.println("Participants: " + foundChat.get().getChatParticipants().size());
            }


            List<Chat> user1Chats = chatRepository.findAllByParticipantID(user1.getUserID());
            System.out.println("User1 chats count: " + user1Chats.size());

            System.out.println("\nUpdating user status...");
            userRepository.updateStatusByID(user1.getUserID(), Status.ONLINE);
            Optional<User> updatedUser = userRepository.findByID(user1.getUserID());
            updatedUser.ifPresent(user ->
                    System.out.println("Updated user status: " + user.getStatus())
            );


            System.out.println("\nTesting deletion...");
            chatRepository.delete(chat.getChatID());
            Optional<Chat> deletedChat = chatRepository.findById(chat.getChatID());
            System.out.println("Chat deleted: " + deletedChat.isEmpty());


            System.out.println("Locking...");
            Object lock = new Object();
            synchronized(lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}