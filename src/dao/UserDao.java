package dao;

import database.DBConnection;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {
    private final ActivityLogDao activityLogDao = new ActivityLogDao();

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("UserID"),
                    rs.getString("FullName"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("Role"),
                    rs.getString("Status")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registerUser(String fullName, String username, String password, String role) {
        String sql = "INSERT INTO Users (FullName, Username, Password, Role, Status) VALUES (?, ?, ?, ?, 'PENDING')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, role);

            boolean success = pstmt.executeUpdate() > 0;
            if (success) {
                activityLogDao.logAction(
                    0,
                    "USER_REGISTERED",
                    "Registered new IT servicing account for: " + username + " with role " + role
                );
            }
            return success;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateUserRoleAndStatus(int adminUserId, int targetUserId, String newRole, String newStatus) {
        String sql = "UPDATE Users SET Role = ?, Status = ? WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newRole);
            pstmt.setString(2, newStatus);
            pstmt.setInt(3, targetUserId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                activityLogDao.logAction(
                    adminUserId,
                    "USER_UPDATED",
                    "Admin ID " + adminUserId + " updated User ID " + targetUserId + " role to " + newRole + " and status to " + newStatus
                );
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUserRoleAndStatus(int targetUserId, String newRole, String newStatus) {
        return updateUserRoleAndStatus(1, targetUserId, newRole, newStatus);
    }

    public void displayAllUsers() {
        String sql = "SELECT UserID, FullName, Username, Role, Status FROM Users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n--- USER LIST ---");
            System.out.println("ID   | Full Name          | Username     | Role         | Status");
            System.out.println("---------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-4d | %-18s | %-12s | %-12s | %s\n",
                        rs.getInt("UserID"),
                        rs.getString("FullName"),
                        rs.getString("Username"),
                        rs.getString("Role"),
                        rs.getString("Status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean userExists(int userId) {
        String sql = "SELECT 1 FROM Users WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }
}