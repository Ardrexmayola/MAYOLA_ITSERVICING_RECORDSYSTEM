package dao;

import database.DBConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean registerUser(String fullName, String username, String password, String role) {
        String sql = "INSERT INTO Users (FullName, Username, Password, Role, Status) VALUES (?, ?, ?, ?, 'Pending')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fullName);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, role);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Registration failed (Username might already exist).");
            return false;
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("Status");
                    if (!status.equalsIgnoreCase("Approved")) {
                        System.out.println("\n[Access Denied] Account is still PENDING approval by an Administrator.");
                        return null;
                    }

                    User user = new User();
                    user.setUserId(rs.getInt("UserID"));
                    user.setFullName(rs.getString("FullName"));
                    user.setUsername(rs.getString("Username"));
                    user.setRole(rs.getString("Role"));
                    user.setStatus(status);
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getPendingUsers() {
        List<User> pendingList = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE Status = 'Pending'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setUsername(rs.getString("Username"));
                user.setRole(rs.getString("Role"));
                pendingList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingList;
    }

    public boolean approveUserByUsername(String username) {
        String sql = "UPDATE Users SET Status = 'Approved' WHERE Username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}