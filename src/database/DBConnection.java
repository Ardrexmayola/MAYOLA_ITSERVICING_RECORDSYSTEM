package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // TODO: Replace "SERVICING DATABASE.db" with the exact filename of your database file
    private static final String URL = "jdbc:sqlite:SERVICING DATABASE.db";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection to SQLite has failed: " + e.getMessage());
        }
        return conn;
    }

    public static void initializeDatabase() {
        // Since your friend already set up the database tables and records,
        // we just verify the connection here without running 'CREATE TABLE' statements
        // that could overwrite or interfere with existing data.
        try (Connection conn = getConnection()) {
            if (conn != null) {
                File dbFile = new File("SERVICING DATABASE.db"); // Match your filename here too
                System.out.println("[SUCCESS] Connected to existing database!");
            }
        } catch (SQLException e) {
            System.out.println("FAILURE: Error connecting to database.");
            e.printStackTrace();
        }
    }
}