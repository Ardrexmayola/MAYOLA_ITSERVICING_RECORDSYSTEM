package app;

import database.DatabaseInitializer;
import dao.UserDao;
import ui.Authentication;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize all database tables and foreign key constraints
        DatabaseInitializer.initializeDatabase();

        Scanner scanner = new Scanner(System.in);
        UserDao userDao = new UserDao();
        Authentication auth = new Authentication(userDao, scanner);

        // Start system entry point
        auth.showAuthMenu();
        scanner.close();
    }
}