package dev.jlipka.pickly.db;

import dev.jlipka.pickly.model.User;

import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.util.List;

public class DbTest {
    public static void main(String[] args) throws InterruptedException {
        Connection connection = DatabaseConnection.getConnection();
        DatabaseSetup databaseSetup = new DatabaseSetup(connection);
        databaseSetup.createTables();
        UserRepository userRepository = new UserRepository(connection);
        AuthenticationService authenticationService = new AuthenticationService(connection);
        authenticationService.registerUser("test", "test", "testUser");
        Thread.sleep(100);
        List<User> allUsers = userRepository.getAllUsers();

        for (User user: allUsers) {
            System.out.println(user);
        }
    }
}
