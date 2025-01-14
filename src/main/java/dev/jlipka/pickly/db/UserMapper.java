package dev.jlipka.pickly.db;

import dev.jlipka.pickly.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Slf4j
public class UserMapper {
    public static User mapFromResultSet(ResultSet rs) throws SQLException {
            return User.builder()
                    .userID((UUID) rs.getObject("userID"))
                    .login(rs.getString("login"))
                    .passwordHash(rs.getString("passwordHash"))
                    .username(rs.getString("username"))
                    .name(rs.getString("name"))
                    .surname(rs.getString("surname"))
                    .status(Status.valueOf(rs.getString("status")))
                    .avatar(rs.getBlob("avatar"))
                    .age(rs.getInt("age"))
                    .createdAt(rs.getTimestamp("createdAt"))
                    .build();

    }
    public static User mapToNewUser(String login, String plainTextPassword, String username) {
        return User.builder()
                .login(login)
                .passwordHash(hashPassword(plainTextPassword))
                .username(username)
                .build();
    }

    private static String hashPassword(String plainTextPassowrd) {
        return BCrypt.hashpw(plainTextPassowrd, BCrypt.gensalt());
    }
}