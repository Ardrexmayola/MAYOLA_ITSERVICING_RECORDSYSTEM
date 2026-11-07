package app;

import dao.UserDao;
import database.DatabaseInitializer;
import ui.Authentication;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize Database and Tables (including Activity_Logs)
        DatabaseInitializer.initialize();

        UserDao userDao = new UserDao();
        Scanner scanner = new Scanner(System.in);
        Authentication auth = new Authentication(userDao, scanner);

        System.out.println(">>> IT Servicing Record System Initialized Successfully.");

        while (true) {
            System.out.println("\n--- WELCOME ---");
            System.out.println("1. Login");
            System.out.println("2. Register New Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    auth.login();
                    break;
                case "2":
                    handlePublicRegistration(userDao, scanner);
                    break;
                case "3":
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void handlePublicRegistration(UserDao userDao, Scanner scanner) {
        System.out.println("\n--- ACCOUNT REGISTRATION ---");
        System.out.print("Enter Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Role (ADMIN/TECHNICIAN): ");
        String role = scanner.nextLine().toUpperCase();

        boolean success = userDao.registerUser(fullName, username, password, role);
        if (success) {
            System.out.println("Registration successful! Account status is PENDING.");
        } else {
            System.out.println("Registration failed. Username might already exist.");
        }
    }
}