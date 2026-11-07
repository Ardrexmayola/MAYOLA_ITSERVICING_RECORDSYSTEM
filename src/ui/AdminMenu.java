package ui;

import dao.ActivityLogDao;
import dao.UserDao;
import java.util.Scanner;

public class AdminMenu {
    private final UserDao userDao;
    private final Scanner scanner;
    private final int adminUserId;
    private final ActivityLogDao activityLogDao;

    public AdminMenu(UserDao userDao, Scanner scanner, int adminUserId) {
        this.userDao = userDao;
        this.scanner = scanner;
        this.adminUserId = adminUserId;
        this.activityLogDao = new ActivityLogDao();
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View All Users");
            System.out.println("2. Update User Role & Status");
            System.out.println("3. View System Activity Logs (Audit Trail)");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    userDao.displayAllUsers();
                    break;
                case "2":
                    System.out.print("Enter Target User ID to update: ");
                    int targetId = Integer.parseInt(scanner.nextLine());
                    if (!userDao.userExists(targetId)) {
                        System.out.println("User ID not found.");
                        break;
                    }
                    System.out.print("Enter New Role (ADMIN/TECHNICIAN): ");
                    String newRole = scanner.nextLine();
                    System.out.print("Enter New Status (APPROVED/PENDING): ");
                    String newStatus = scanner.nextLine();
                    boolean updated = userDao.updateUserRoleAndStatus(adminUserId, targetId, newRole, newStatus);
                    if (updated) {
                        System.out.println("User updated and audited successfully!");
                    } else {
                        System.out.println("Failed to update user.");
                    }
                    break;
                case "3":
                    activityLogDao.displayLogs();
                    break;
                case "4":
                    System.out.println("Logging out from Admin menu...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}