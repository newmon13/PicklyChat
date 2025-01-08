package dev.jlipka.pickly.db;

import dev.jlipka.pickly.model.Status;
import dev.jlipka.pickly.model.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class UserMapper {
    public User mapFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setAvatar(rs.getBytes("avatar"));
        user.setAge(rs.getInt("age"));
        user.setStatus(Status.valueOf(rs.getString("status")));
        return user;
    }
}