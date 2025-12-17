import java.util.NoSuchElementException;
import java.util.Scanner;
import models.AdminUser;
import models.RegularUser;
import models.User;
import services.AuthService;
import services.ProjectService;
import utils.ConsoleMenu;
import utils.ExitHandler;
import utils.FileUtils;

/**
 * Entry point for the console-based Project/Task management app.
 * Initializes sample data, wires default users, and drives the main menu loop.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static boolean isRunning = true;

    /**
     * Print the currently logged-in user and their role.
     */
    private static void displayCurrentUser() {
        User active = AuthService.getCurrentUser();
        if (active != null) {
            System.out.println("Current User: " + active.getDisplayLabel() + "\n\n");
        }
    }

    /**
     * Application entry point. Sets up users, registers a shutdown hook,
     * and runs the main menu loop until the user exits.
     */
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(ExitHandler::printOnce));

        // Programmatic login – also registered for user panel switching
        User admin = new AdminUser("Ernest Essien", "ernest@example.com");
        User regular = new RegularUser("Sample User", "user@example.com");
        AuthService.setAvailableUsers(admin, regular);

        // Default logged-in user (can be switched via User Panel)
        AuthService.login(admin);

        FileUtils.loadProjects();
        while (isRunning) {
            try {
                ConsoleMenu.displayHeader();
                displayCurrentUser();
                ConsoleMenu.displayMainMenu();
                isRunning = ProjectService.handleUserInput(scanner, isRunning);
            }
             catch (NoSuchElementException eof) {
                // Input stream closed; exit quietly and let shutdown hook print message
                isRunning = false;
                ExitHandler.printOnce();
            }
        }
    }
}
