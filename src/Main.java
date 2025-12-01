import java.util.Scanner;
import models.HardwareProject;
import models.SoftwareProject;
import models.Task;
import models.Task.Status;
import services.ProjectService;
import utils.ConsoleMenu;

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

        //create tasks under project
        Task alphaTrackerTask1 = new Task("Set up DB", Task.Status.IN_PROGRESS, alphaTracker.getId());
        Task alphaTrackerTask2 = new Task("Create abstract tracking class", Task.Status.COMPLETED, alphaTracker.getId());

        // System.out.println(alphaTrackerTask1.updateTask(alphaTrackerTask1.getId(), Status.COMPLETED));
        // System.out.println(alphaTrackerTask1.getStatus());
        
        Task temperatureRecorderTask1 = new Task("Design Thermometer", Task.Status.PENDING, temperatureRecorder.getId());

        Task clockItTask1 = new Task("Create date filter", Task.Status.IN_PROGRESS, clockIt.getId());

        Task automaticDoorTask = new Task("Research in to strong doors", Task.Status.PENDING, automaticDoor.getId());


    }

    private static void displayCurrentUser() {
        System.out.println("Current User: " + currentUser + "\n\n");
    }


   
    public static void main(String[] args) {

        while (isRunning) {
            createBaseProjects();
            ConsoleMenu.displayHeader();
            displayCurrentUser();
            ConsoleMenu.displayMainMenu();
            ProjectService.handleUserInput(scanner, isRunning);
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
