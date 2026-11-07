package dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ActivityLogDao {

    public void logAction(int userId, String action, String description) {
        String sql = "INSERT INTO Activity_Logs (UserID, Action, Description) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, action);
            pstmt.setString(3, description);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayLogs() {
        String sql = "SELECT * FROM Activity_Logs ORDER BY LogID DESC";
        System.out.println("\n--- SYSTEM ACTIVITY LOGS (AUDIT TRAIL) ---");
        System.out.println("ID   | User ID | Action Type       | Description                                              | Timestamp");
        System.out.println("--------------------------------------------------------------------------------------------------------------------");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.printf("%-4d | %-7d | %-17s | %-56s | %s\n",
                        rs.getInt("LogID"),
                        rs.getInt("UserID"),
                        rs.getString("Action"),
                        rs.getString("Description"),
                        rs.getString("Timestamp"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}