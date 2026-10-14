package app;

import database.DBConnection;
import dao.UserDAO;
import model.User;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== INITIALIZING IT INFRASTRUCTURE SYSTEM ===");
        DBConnection.initializeDatabase();

        Scanner scanner = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        boolean running = true;

        while (running) {
            System.out.println("\n===== IT INFRASTRUCTURE & DEVICE SERVICE RECORDS =====");
            System.out.println("1. Log In");
            System.out.println("2. Register Account");
            System.out.println("3. Exit System");
            System.out.print("Select an option: ");

            while (!scanner.hasNextInt()) {
                scanner.next();
                System.out.print("Select an option: ");
            }
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    handleLogin(scanner, userDAO);
                    break;
                case 2:
                    handleRegister(scanner, userDAO);
                    break;
                case 3:
                    System.out.println("\nExiting system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid choice. Please select 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    public static void handleLogin(Scanner scanner, UserDAO userDAO) {
        System.out.println("\n--- SYSTEM LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User user = userDAO.login(username, password);

        if (user != null) {
            System.out.println("\n[SUCCESS] Welcome, " + user.getFullName() + " (" + user.getRole() + ")");
            routeUserMenu(scanner, user, userDAO);
        } else {
            System.out.println("\n[LOGIN FAILED] Incorrect credentials or pending account status.");
        }
    }

    public static void handleRegister(Scanner scanner, UserDAO userDAO) {
        System.out.println("\n--- USER REGISTRATION ---");
        System.out.print("Enter Full Name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        System.out.println("Select System Role:");
        System.out.println("1. Administrator");
        System.out.println("2. Network Technician");
        System.out.println("3. IT Staff");
        System.out.print("Enter choice (1-3): ");

        int roleChoice = 1;
        if (scanner.hasNextInt()) {
            roleChoice = scanner.nextInt();
        }
        scanner.nextLine();

        String role;
        switch (roleChoice) {
            case 2: role = "Network Technician"; break;
            case 3: role = "IT Staff"; break;
            default: role = "Administrator"; break;
        }

        boolean success = userDAO.registerUser(fullName, username, password, role);
        if (success) {
            System.out.println("\nAccount successfully registered! Status is PENDING approval.");
        } else {
            System.out.println("\nRegistration failed. Username may already be taken.");
        }
    }

    public static void routeUserMenu(Scanner scanner, User user, UserDAO userDAO) {
        String role = user.getRole().trim().toLowerCase();

        if (role.contains("admin")) {
            showAdminMenu(scanner, userDAO);
        } else if (role.contains("technician")) {
            showTechnicianMenu(scanner);
        } else if (role.contains("staff")) {
            showStaffMenu(scanner);
        } else {
            System.out.println("\nUnknown role permissions.");
        }
    }

    public static void showAdminMenu(Scanner scanner, UserDAO userDAO) {
        int choice;
        do {
            System.out.println("\n=================================");
            System.out.println("     ADMINISTRATOR PANEL         ");
            System.out.println("=================================");
            System.out.println("1. Approve Pending User Accounts");
            System.out.println("2. Manage Devices");
            System.out.println("3. Generate Reports");
            System.out.println("4. Logout");
            System.out.print("Select an option (1-4): ");

            while (!scanner.hasNextInt()) { scanner.next(); }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    handleApproveUsers(scanner, userDAO);
                    break;
                case 2:
                    System.out.println("\n[Feature] Manage Devices module.");
                    break;
                case 3:
                    System.out.println("\n[Feature] Generate Reports module.");
                    break;
                case 4:
                    System.out.println("\nLogging out...");
                    break;
                default:
                    System.out.println("\nInvalid option.");
            }
        } while (choice != 4);
    }

    public static void handleApproveUsers(Scanner scanner, UserDAO userDAO) {
        System.out.println("\n--- PENDING USER ACCOUNTS ---");
        List<User> pendingUsers = userDAO.getPendingUsers();

        if (pendingUsers.isEmpty()) {
            System.out.println("No pending user registrations found.");
            return;
        }

        System.out.println(String.format("%-20s | %-20s | %-20s", "Full Name", "Username", "Role"));
        System.out.println("------------------------------------------------------------------");
        for (User u : pendingUsers) {
            System.out.println(String.format("%-20s | %-20s | %-20s", u.getFullName(), u.getUsername(), u.getRole()));
        }

        System.out.print("\nEnter Username to APPROVE (or type 'cancel' to go back): ");
        String targetUsername = scanner.nextLine().trim();

        if (!targetUsername.equalsIgnoreCase("cancel") && !targetUsername.isEmpty()) {
            boolean success = userDAO.approveUserByUsername(targetUsername);
            if (success) {
                System.out.println("\n[SUCCESS] User account '" + targetUsername + "' is now APPROVED!");
            } else {
                System.out.println("\n[FAILED] Could not approve user. Check username.");
            }
        }
    }

    public static void showTechnicianMenu(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n=================================");
            System.out.println("    NETWORK TECHNICIAN MENU      ");
            System.out.println("=================================");
            System.out.println("1. Record Maintenance Activities");
            System.out.println("2. Logout");
            System.out.print("Select an option (1-2): ");

            while (!scanner.hasNextInt()) { scanner.next(); }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: System.out.println("\n[Feature] Record Maintenance module."); break;
                case 2: System.out.println("\nLogging out..."); break;
                default: System.out.println("\nInvalid option.");
            }
        } while (choice != 2);
    }

    public static void showStaffMenu(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n=================================");
            System.out.println("          IT STAFF MENU          ");
            System.out.println("=================================");
            System.out.println("1. View Infrastructure Records");
            System.out.println("2. View Service Requests");
            System.out.println("3. Logout");
            System.out.print("Select an option (1-3): ");

            while (!scanner.hasNextInt()) { scanner.next(); }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: System.out.println("\n[Feature] View Infrastructure Records."); break;
                case 2: System.out.println("\n[Feature] View Service Requests."); break;
                case 3: System.out.println("\nLogging out..."); break;
                default: System.out.println("\nInvalid option.");
            }
        } while (choice != 3);
    }
}