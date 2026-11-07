package ui;

import dao.ActivityLogDao;
import dao.UserDao;
import model.User;
import java.util.Scanner;

public class Authentication {
    private final UserDao userDao;
    private final ActivityLogDao activityLogDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.activityLogDao = new ActivityLogDao();
        this.scanner = scanner;
    }

    public void login() {
        System.out.println("\n--- USER LOGIN ---");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = userDao.authenticate(username, password);
        if (user == null) {
            System.out.println("Invalid credentials.");
            return;
        }

        // Audit Log: Login
        activityLogDao.logAction(
            user.UserID(),
            "USER_LOGIN",
            "User " + user.Username() + " (ID: " + user.UserID() + ") logged in."
        );

        routeToMenu(user);

        // Audit Log: Logout
        activityLogDao.logAction(
            user.UserID(),
            "USER_LOGOUT",
            "User " + user.Username() + " (ID: " + user.UserID() + ") logged out."
        );
    }

    private void routeToMenu(User user) {
        if ("ADMIN".equalsIgnoreCase(user.Role())) {
            AdminMenu adminMenu = new AdminMenu(userDao, scanner, user.UserID());
            adminMenu.displayMenu();
        } else {
            System.out.println(">>> Redirected to IT Servicing Module. Press Enter to exit...");
            scanner.nextLine();
        }
    }
}