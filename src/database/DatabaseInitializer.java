package database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        String[] sqlStatements = {
            "CREATE TABLE IF NOT EXISTS Users (" +
                "UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FullName TEXT NOT NULL, " +
                "Username TEXT UNIQUE NOT NULL, " +
                "Password TEXT NOT NULL, " +
                "Role TEXT NOT NULL, " +
                "Status TEXT DEFAULT 'PENDING'" +
            ");",

            "CREATE TABLE IF NOT EXISTS Devices (" +
                "DeviceID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "DeviceName TEXT NOT NULL, " +
                "DeviceType TEXT NOT NULL, " +
                "SerialNumber TEXT UNIQUE NOT NULL, " +
                "Status TEXT NOT NULL, " +
                "Location TEXT NOT NULL" +
            ");",

            "CREATE TABLE IF NOT EXISTS ServiceRecords (" +
                "ServiceID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "DeviceID INTEGER, " +
                "TechnicianID INTEGER, " +
                "ServiceDate TEXT NOT NULL, " +
                "IssueDescription TEXT NOT NULL, " +
                "Resolution TEXT, " +
                "FOREIGN KEY (DeviceID) REFERENCES Devices(DeviceID), " +
                "FOREIGN KEY (TechnicianID) REFERENCES Users(UserID)" +
            ");",

            "CREATE TABLE IF NOT EXISTS Activity_Logs (" +
                "LogID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "Action TEXT NOT NULL, " +
                "Description TEXT NOT NULL, " +
                "Timestamp TEXT NOT NULL, " +
                "FOREIGN KEY (UserID) REFERENCES Users(UserID)" +
            ");"
        };

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON;");

            for (String sql : sqlStatements) {
                stmt.execute(sql);
            }
            System.out.println("[DatabaseInitializer] IT Servicing tables initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
}