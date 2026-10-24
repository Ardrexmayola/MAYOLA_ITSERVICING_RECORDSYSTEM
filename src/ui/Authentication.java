package ui;

import dao.UserDao;
import model.User;
import java.util.Scanner;

public class Authentication {
    private final UserDao userDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void showAuthMenu() {
        while (true) {
            System.out.println("\n=== IT SERVICING SYSTEM AUTHENTICATION ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> handleLogin();
                case "2" -> handleRegister();
                case "3" -> {
                    System.out.println("Exiting system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private void handleLogin() {
        System.out.println("\n--- USER LOGIN ---");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = userDao.authenticate(username, password);
        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        if ("PENDING".equalsIgnoreCase(user.getStatus())) {
            System.out.println("Your account is currently PENDING approval by an Administrator.");
            return;
        }

        routeToRoleMenu(user);
    }

    private void handleRegister() {
        System.out.println("\n--- USER REGISTRATION ---");
        System.out.print("Enter Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Role (Admin / Technician / IT Staff): ");
        String role = scanner.nextLine();

        boolean success = userDao.registerUser(fullName, username, password, role);
        if (success) {
            System.out.println("Registration successful! Account status is PENDING approval.");
        } else {
            System.out.println("Registration failed. Username may already exist.");
        }
    }

    private void routeToRoleMenu(User user) {
        System.out.println("\nWelcome, " + user.getFullName() + "! [Role: " + user.getRole() + "]");
        switch (user.getRole().toUpperCase()) {
            case "ADMIN" -> {
                AdminMenu adminMenu = new AdminMenu(userDao, scanner);
                adminMenu.displayMenu();
            }
            case "TECHNICIAN" -> System.out.println(">>> Redirected to TECHNICIAN SERVICE MODULE...");
            case "IT STAFF" -> System.out.println(">>> Redirected to DEVICE MANAGEMENT MODULE...");
            default -> System.out.println("Unknown role menu.");
        }
    }
}