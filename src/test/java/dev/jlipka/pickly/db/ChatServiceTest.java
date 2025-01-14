package dev.jlipka.pickly.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.spy;


public class ChatServiceTest {
    private static Connection connection;
    private static ChatRepository chatRepository;
    private static UserRepository userRepository;
    private static MessageRepository messageRepository;
    private static AttachmentRepository attachmentRepository;
    private static ChatService chatService;

    @BeforeAll
    public static void setUp() throws SQLException {
        DatabaseConnectionManager.initialize(DatabaseConfig.createDefault());
        connection = DatabaseConnectionManager.getConnection();
        DatabaseSetup databaseSetup = new DatabaseSetup(connection);
        databaseSetup.createTables();

        chatRepository = spy(new ChatRepository(connection));
        userRepository = spy(new UserRepository(connection));
        messageRepository = spy(new MessageRepository(connection));
        attachmentRepository = spy(new AttachmentRepository(connection));

        chatService = new ChatService(
                chatRepository,
                userRepository,
                messageRepository,
                attachmentRepository
        );
    }


    @Test
    public void shouldAddNewUser() {
        User user = UserMapper.mapToNewUser("john", "password", "johndoe");
        userRepository.save(user);

        User searchUser = userRepository.findByID(user.getUserID()).get();
        Assertions.assertEquals(user.getUserID(), searchUser.getUserID());
    }

}
