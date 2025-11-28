import java.util.Scanner;
import models.HardwareProject;
import models.Project;
import models.SoftwareProject;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static String currentUser = "Ernest Essien (Admin)";
    private static Boolean isRunning = true;

    public static void createBaseProjects()
    {
        // create 4 projects
        SoftwareProject alphaTracker = new SoftwareProject("Alpha Tracker", "Track my tasks for this project", 15000, 5);
        HardwareProject temperatureRecorder = new HardwareProject("Temperature Recorder", "Record the temperature of the day at 1 pm", 10000, 3);    
        SoftwareProject clockIt = new SoftwareProject("Clock IT", "Record what employees do during the working day", 7000, 7);
        HardwareProject automaticDoor = new HardwareProject("Smart Door", "Automatically record employees who enter offices", 12000, 10);
    }

    private static void displayHeader() {
        System.out.println("\n\n==================================");
        System.out.println("| JAVA PROJECT MANAGEMENT SYSTEM |");
        System.out.println("==================================\n\n");
    }

    private static void displayCurrentUser() {
        System.out.println("Current User: " + currentUser + "\n\n");
    }

      private static void displayMainMenu() {
        System.out.println("Main Menu:");
        System.out.println("-----------");
        System.out.println("1. Manage Projects");
        System.out.println("2. Manage Tasks");
        System.out.println("3. View Status Reports");
        System.out.println("4. Exit Application");
        System.out.print("\n\nEnter your choice: ");
    }

    private static void handleUserInput()
    {
        if (scanner.hasNextInt())
        {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n>> Navigating to Manage Projects...");
                    Project.displayProjectHeader();
                    Project.displayProjectMenu();
                    Project.handleProjectUserInput(isRunning, scanner);
                    break;
                case 2:
                    System.out.println("\n>> Navigating to Manage Tasks...");
                    break;
                case 3:
                    System.out.println("\n>> Navigating to View Status Reports...");
                    break;
                case 4:
                    isRunning = false;
                    System.out.println("\nThank you using Project Management today!!\n");
                    break;
                default:
                    System.out.println("\n>> Invalid input. Please a number between 1 - 4");
            }
        } else {
            System.out.println("\n>> Invalid input, Please enter a number");
            scanner.next();
        }
        if(isRunning)
        {
            // System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    public static void main(String[] args) {

        while (isRunning) {
            createBaseProjects();
            displayHeader();
            displayCurrentUser();
            displayMainMenu();
            handleUserInput();
        }

        // System.out.println("\nWelcome to my task manager\n");
        // SoftwareProject alphaTracker = new SoftwareProject("Alpha Tracker", "Track my tasks for this project", 15000, 5);
        // HardwareProject temperatureRecorder = new HardwareProject("Temperature Recorder", "Record the temperature of the day at 1 pm", 10000, 3);
        

        // SoftwareProject clockIt = new SoftwareProject("Clock IT", "Record what employees do during the working day", 7000, 7);
        // HardwareProject automaticDoor = new HardwareProject("Smart Door", "Automatically record employees who enter offices", 12000, 10);

        // // Project.displayProjects();
        // Project.filterByBudget(7000, 12000);

        // Project.filterByType("Software");
        // Project.filterByType("Hardware");
        // Project.filterByType("Sofwaree");



        // alphaTracker.displayProject();
        // alphaTracker.getProjectDetails();

        // temperatureRecorder.displayProject();
        // temperatureRecorder.getProjectDetails();

    }
}
