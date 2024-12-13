package dev.jlipka.pickly.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class DataBase {
    private Connection conn;

    public DataBase() throws Exception {
        loadENV();
    }

    private void loadENV() {
        String username = System.getenv().getOrDefault("DB_USER", "user");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "admin");
        String url = System.getenv().getOrDefault(
                "DB_LOCATION",
                "jdbc:postgresql://localhost:54320/chatapp"
        );

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean insertNewUser(String name, String password){
        try(PreparedStatement stm = conn.prepareStatement("insert into users (userName, password, avatar_path) VALUES (?, ?, 'src/main/resources/com/app/chatapp/pictures/avatar.jpg' )")){
            stm.setString(1, name);
            stm.setString(2, password);
            stm.executeUpdate();
            log.info("User {} successfully added to database", name);
            return true;
        }catch (SQLException e){
            log.error("Error occurred while adding user: {} to database. {}",name, e.getMessage());
            return false;
        }
    }

    public String getUserFilePath(String username){
        try(PreparedStatement stm = conn.prepareStatement("SELECT avatar_path FROM users WHERE username=?")){
            stm.setString(1, username);
            ResultSet res = stm.executeQuery();
            if(res.next()){
                return res.getString(1);
            }
        } catch (SQLException e){
            log.error("Error occurred while checking user filePath: {} in database. {}",username, e.getMessage());
        }
        return null;
    }

    public boolean insertNewUser(String name, String password, String filePath){
        try(PreparedStatement stm = conn.prepareStatement("insert into users (userName, password, avatar_path) VALUES (?, ?, ?)")){
            stm.setString(1, name);
            stm.setString(2, password);
            stm.setString(3, filePath);
            stm.executeUpdate();
            log.info("User {} successfully added to database", name);
            return true;
        }catch (SQLException e){
            log.error("Error occurred while adding user: {} to database. {}", name, e.getMessage());
            return false;
        }
    }

    public boolean updateUserName(String oldName, String newName){
        try(PreparedStatement stm = conn.prepareStatement("UPDATE users set username=? where username=?")){
            stm.setString(1, newName);
            stm.setString(2, oldName);
            log.info("User {} successfully updated in database", newName);
            return true;
        }catch (SQLException e){
            log.error("Error occurred while updating user: {} to database", oldName, e.getMessage());
            return false;
        }
    }

    public boolean doesUsernameExist(String newName){
        try(PreparedStatement stm = conn.prepareStatement("SELECT * FROM users WHERE username=?")){
            stm.setString(1, newName);
            ResultSet res = stm.executeQuery();
            if(res.next()){
                log.info("User: " + newName + " exists in database");
                return true;
            }
        } catch (SQLException e){
            log.error("Error occurred while checking user: " + newName + " in database", e.getMessage());
        }
        return false;
    }

    public String getUserPassword(String username){
        try(PreparedStatement stm = conn.prepareStatement("SELECT password FROM users WHERE username=?")){
            stm.setString(1, username);
            ResultSet res = stm.executeQuery();
            if(res.next()){
                return res.getString(1);
            }
        } catch (SQLException e){
            log.error("Error occurred while checking user password: " + username + " in database", e.getMessage());
        }
        return null;
    }

    public void shutdown(){
        try{
            conn.close();
        }catch (SQLException e){
            log.error("Error occurred while closing database connection", e.getMessage());
        }
    }
}