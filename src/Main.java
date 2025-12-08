import static utils.ConsoleColors.RED;
import static utils.ConsoleColors.RESET;

import java.util.NoSuchElementException;
import java.util.Scanner;
import models.HardwareProject;
import models.SoftwareProject;
import models.Task;
import models.AdminUser;
import models.RegularUser;
import models.User;
import services.AuthService;
import services.ProjectService;
import services.TaskService;
import utils.ConsoleMenu;
import utils.ExitHandler;
import utils.exceptions.ProjectFullException;
import utils.exceptions.TaskFullException;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static boolean isRunning = true;

    /**
     * Seed the application with some sample projects and tasks
     * so menus are not empty on first run.
     */
    public static void createBaseProjects()
    {
        // create 4 projects
        try {
            SoftwareProject alphaTracker = new SoftwareProject("Alpha Tracker", "Track my tasks for this project", 15000, 5);
            HardwareProject temperatureRecorder = new HardwareProject("Temperature Recorder", "Record the temperature of the day at 1 pm", 10000, 3);    
            SoftwareProject clockIt = new SoftwareProject("Clock IT", "Record what employees do during the working day", 7000, 7);
            HardwareProject automaticDoor = new HardwareProject("Smart Door", "Automatically record employees who enter offices", 12000, 10);
            
            //create tasks under project
            Task alphaTrackerTask1 = Task.create("Set up DB", Task.Status.IN_PROGRESS, alphaTracker.getId());
            Task alphaTrackerTask2 = Task.create("Create abstract tracking class", Task.Status.COMPLETED, alphaTracker.getId());            
            Task temperatureRecorderTask1 = Task.create("Design Thermometer", Task.Status.PENDING, temperatureRecorder.getId());
            Task clockItTask1 = Task.create("Create date filter", Task.Status.IN_PROGRESS, clockIt.getId());
            Task automaticDoorTask = Task.create("Research in to strong doors", Task.Status.PENDING, automaticDoor.getId());

            ProjectService.addProjectToStorage(alphaTracker);
            ProjectService.addProjectToStorage(temperatureRecorder);
            ProjectService.addProjectToStorage(clockIt);
            ProjectService.addProjectToStorage(automaticDoor);

            TaskService.addTaskToStorage(automaticDoorTask);
            TaskService.addTaskToStorage(alphaTrackerTask1);
            TaskService.addTaskToStorage(alphaTrackerTask2);
            TaskService.addTaskToStorage(temperatureRecorderTask1);
            TaskService.addTaskToStorage(clockItTask1);            
        } catch (ProjectFullException e) {
            System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
        } catch (TaskFullException e){
            System.out.println(RED + "ERROR: " + e.getMessage() + RESET);

        }




    }

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
     * Application entry point. Sets up the logged-in user and
     * runs the main menu loop until the user exits.
     */
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(ExitHandler::printOnce));

        // Programmatic login – also registered for user panel switching
        User admin = new AdminUser("Ernest Essien", "ernest@example.com");
        User regular = new RegularUser("Sample User", "user@example.com");
        AuthService.setAvailableUsers(admin, regular);

        // Default logged-in user (can be switched via User Panel)
        AuthService.login(admin);

        while (isRunning) {
            try {
                createBaseProjects();
                ConsoleMenu.displayHeader();
                displayCurrentUser();
                ConsoleMenu.displayMainMenu();
                isRunning = ProjectService.handleUserInput(scanner, isRunning);
            } catch (NoSuchElementException eof) {
                // Input stream closed; exit quietly and let shutdown hook print message
                isRunning = false;
                ExitHandler.printOnce();
            }
        }
    }
}
