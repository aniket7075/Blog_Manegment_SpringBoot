package com.dao;

import com.entity.User;
import com.helper.ConnectionProvider;

import java.sql.*;

public class UserDao {

    private Connection con;

    public UserDao(Connection con) {
        this.con = con;
    }

   
    public int saveUser(User user) {
        int result = 0;
        String query = "INSERT INTO user(name, email, password, gender, about) VALUES(?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = con.prepareStatement(query);

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getGender());
            pstmt.setString(5, user.getAbout() != null ? user.getAbout() : "");

            result = pstmt.executeUpdate();

            if (result > 0) {
                result = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return result;
    }

   
    public User getUserByEmailAndPassword(String email, String password) {
        User user = null;
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this
        }

        return user;
    }

  
    public User getUserById(int userId) {
        User user = null;
        String query = "SELECT * FROM user WHERE id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return user;
    }

  
    public int updateUser(User user, int id) {
        int result = 0;
        String query = "UPDATE user SET name = ?, email = ?, password = ?, gender = ?, about = ?, profile = ? WHERE id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getGender());
            pstmt.setString(5, user.getAbout() != null ? user.getAbout() : "");
            pstmt.setString(6, user.getProfile());
            pstmt.setInt(7, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return result;
    }


    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setGender(rs.getString("gender"));
        user.setAbout(rs.getString("about"));
        user.setRegdate(rs.getTimestamp("regdate"));
        user.setProfile(rs.getString("profile"));
        return user;
    }

    
}
