package dev.jlipka.pickly.db;

import javafx.beans.binding.When;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    private static Connection connection;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private static AuthenticationService authenticationService;

    @BeforeAll
    public static void setUp() throws SQLException {
        DatabaseConnectionManager.initialize(DatabaseConfig.createDefault());
        connection = DatabaseConnectionManager.getConnection();
        DatabaseSetup databaseSetup = new DatabaseSetup(connection);
        databaseSetup.createTables();
    }

    @Test
    public void shouldRegisterNewUser() {
        //when
        User registeredUser = authenticationService.register("newLogin", "newPassword", "newUsername");
        //then
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void shouldLoginIntoExistingAccount() {
        //given
        User user = UserMapper.mapToNewUser("existingLogin", "existingPassword", "existingUsername");
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        //when
        User existingUser = authenticationService.authenticate("existingLogin", "existingPassword");
        //then
        assertEquals(existingUser, user);
    }



    @AfterEach
    public void cleanUsers() throws SQLException {
        try(Statement stat = connection.createStatement()) {
            stat.execute("DELETE FROM userAccounts");
        }
    }
    @AfterAll
    public static void closeResources() throws SQLException {
        connection.close();
        DatabaseConnectionManager.closePool();
    }
}
