package services;

import java.util.Scanner;
import models.Project;
import models.SoftwareProject;
import models.HardwareProject;
import static utils.ConsoleColors.*;

import utils.ConsoleColors;
import utils.ConsoleMenu;
import utils.ValidationUtils;
import utils.exceptions.EmptyProjectListException;
import utils.exceptions.InvalidBudgetRangeException;
import utils.exceptions.InvalidProjectTypeException;
import utils.exceptions.ProjectFullException;
import utils.exceptions.ProjectNotFoundException;
import utils.exceptions.TaskNotFoundException;

public class ProjectService {
    
    // In-memory storage for projects (fixed capacity for this exercise)
    private static final int MAX_PROJECTS = 20;  
    private static Project[] allProjects = new Project[MAX_PROJECTS]; 
    private static int projectCount = 0;

    

    public static void addProjectToStorage(Project project) throws ProjectFullException {
        if (projectCount >= MAX_PROJECTS)
            throw new ProjectFullException(project.getName());
        allProjects[projectCount] = project;
        projectCount++; 
    }

    public static Project findProject(String id) throws ProjectNotFoundException {
        for (int i = 0; i < projectCount; i++) {
            Project p = allProjects[i];
            if (p != null && p.getId().equals(id)) {
                return p;
            }
        }   
        throw new ProjectNotFoundException();
    }

    public static boolean projectExists(String projectID) throws ProjectNotFoundException {
        try {
            findProject(projectID);
            return true;
        } catch (ProjectNotFoundException e){
            throw new ProjectNotFoundException();
        }
    }

    public static int getProjectCount() {
        return projectCount;
    }

    public static void addProject(Scanner scanner) throws InvalidProjectTypeException, ProjectFullException {
        System.out.print(BOLD + YELLOW + "Enter project name: " + RESET);
        String pName = scanner.nextLine();

        System.out.print(BOLD + YELLOW + "Enter project description: " + RESET);
        String pDescription = scanner.nextLine();
        
        System.out.print(BOLD + YELLOW + "Enter budget (numbers): " + RESET);
        long pBudget = scanner.nextLong();
        scanner.nextLine();

        System.out.print(BOLD + YELLOW + "Enter team size (numbers): " + RESET);
        int pTeamSize = scanner.nextInt();
        scanner.nextLine();

        System.out.print(BOLD + YELLOW + "Enter project type (Software or Hardware - type in full): " + RESET);
        String pProjectType = scanner.next();
        scanner.nextLine();

        Project newProject;

        if (pProjectType.equals("Software")) {
            newProject = new SoftwareProject(pName, pDescription, pBudget, pTeamSize);
        } else if (pProjectType.equals("Hardware")) {
            newProject = new HardwareProject(pName, pDescription, pBudget, pTeamSize);
        } else {
            throw new InvalidProjectTypeException(pProjectType);
        }

        ProjectService.addProjectToStorage(newProject);

        if (newProject != null) {
            System.out.print(GREEN + BOLD + "\n\n>> Project '" + newProject.getName() + "' created successfully!\n" + RESET);
        }

        
    }

    public static Project[] getProjects()
    {
        Project[] currentProjects = new Project[projectCount];
        for (int i = 0; i < projectCount; i++)
        {
            Project p = allProjects[i];
            currentProjects[i] = p;
        }
        return currentProjects;
    }

    public static void displayProjects(Scanner scanner, Boolean isRunning, boolean fromTask) {
        System.out.printf(GREEN+"\nAll projects (%s)%n%n" + RESET, projectCount);
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.println("|ID\t\t\t\t| PROJECT NAME\t| DESCRIPTION\t\t\t| TYPE\t| TEAM SIZE\t| BUDGET|");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < projectCount; i++) {
            Project p = allProjects[i];
            System.out.printf("|%s| %s| %s| %s| %d| %d|", p.getId(), p.getName(), p.getDescription(), p.getProjectType(), p.getTeamSize(), p.getBudget());
            System.out.println("\n-----------------------------------------------------------------------------------------------------------------\n");
        }
        if (!fromTask)
            displayProjectDetails(scanner, isRunning);
    }

    public static void displayProjectDetails(Scanner scanner, Boolean isRunning) {
        System.out.print(BOLD + YELLOW + "Enter project ID to view details (or 0 to return): " + RESET);
        
        if (scanner.hasNext()) {
            String choice = scanner.next();
            scanner.nextLine();

            switch (choice) {
                case "0":
                    ConsoleMenu.displayProjectHeader();
                    ConsoleMenu.displayProjectMenu();
                    handleProjectUserInput(isRunning, scanner);
                    break;
                default:
                    try {
                        Project foundProject = findProject(choice);
                        TaskService.filterByProject(foundProject.getId(), scanner, isRunning);
                        ConsoleMenu.getProjectDetails(foundProject);
                    } catch (TaskNotFoundException e) {
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    } catch (ProjectNotFoundException e){
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    } 
                    displayProjectDetails(scanner, isRunning);                     
                    break;
            }
        }
    }

    public static void filterByType(String projectType, Scanner scanner, Boolean isRunning) throws InvalidProjectTypeException {
        if (!ValidationUtils.isValidProjectType(projectType)) {
            throw new InvalidProjectTypeException(projectType);
        }
                
        System.out.println(CYAN + BOLD + "\n\n==================================");
        System.out.printf("|\t%s Projects\t|\n", projectType);
        System.out.println("==================================" + RESET + "\n\n");

        for (int i = 0; i < projectCount; i++) {
            Project p = allProjects[i];
            if (p != null && projectType.equals(p.getProjectType())) {
                ConsoleMenu.displayProject(p);
            }
        }

        displayProjectDetails(scanner, isRunning);
    }

    public static void filterByBudget(long minAmount, long maxAmount, Scanner scanner, Boolean isRunning) throws InvalidBudgetRangeException {
        if (!ValidationUtils.isValidBudgetRange(minAmount, maxAmount)) {
            return;
        }

        int count = 0;
        System.out.println(CYAN + BOLD + "\n\n==================================");
        System.out.println("|\tProjects within range\t|");
        System.out.println("==================================" + RESET + "\n\n");

        for (int i = 0; i < projectCount; i++) {
            Project p = allProjects[i]; 
            if (p != null && minAmount <= p.getBudget() && p.getBudget() <= maxAmount) {
                count++;
                ConsoleMenu.displayProject(p);       
            }
        }

        if (count == 0) {
            System.out.println(RED + BOLD + "No projects found within budget range\n\n" + RESET);
            
        } else {
            displayProjectDetails(scanner, isRunning);
        }
    }

    public static void handleProjectUserInput(Boolean isRunning, Scanner scanner) {
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    ConsoleMenu.displayProjectAddHeader();
                    try {
                        ProjectService.addProject(scanner);
                    } catch (InvalidProjectTypeException e){
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    } catch (ProjectFullException e) {
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    }

                    System.out.print(BOLD + CYAN + "\n\n>> Press Enter to continue... " + RESET);
                    scanner.nextLine();
                    ConsoleMenu.displayProjectHeader();
                    ConsoleMenu.displayProjectMenu();
                    handleProjectUserInput(isRunning, scanner);
                    break;
                case 2:
                    displayProjects(scanner, isRunning, false);
                    break;
                case 3:
                    try {
                        filterByType("Software", scanner, isRunning);
                    } catch (InvalidProjectTypeException e){
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    }
                    break;
                case 4:
                    try {
                        filterByType("Hardware", scanner, isRunning);
                    } catch (InvalidProjectTypeException e) {
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    }
                    break;
                case 5:
                    try {
                        System.out.print(BOLD + YELLOW + "Enter mininum amount (numbers): " + RESET);
                        long min = scanner.nextLong();
                        scanner.nextLine();
                        System.out.print(BOLD + YELLOW + "Enter maximum amount (numbers): " + RESET);
                        long max = scanner.nextLong();
                        scanner.nextLine();
                        filterByBudget(min, max, scanner, isRunning);
                    } catch (java.util.InputMismatchException e) {
                        System.out.println(RED + "ERROR: Invalid input. Please enter numeric values only." + RESET);
                        scanner.nextLine(); // Clear the invalid input
                    } catch (InvalidBudgetRangeException e) {
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    }
                    System.out.print(BOLD + CYAN + ">> Press Enter to continue... " + RESET);
                    scanner.nextLine();

                    ConsoleMenu.displayProjectHeader();
                    ConsoleMenu.displayProjectMenu();
                    handleProjectUserInput(isRunning, scanner);
                    break;
                default:
                    System.out.println(RED + BOLD + "\n>> Invalid input. Please enter a number between 1 - 5" + RESET);
            }
        } else {
            System.out.println(RED + BOLD + "\n>> Invalid input, Please enter a number" + RESET);
            scanner.next();
        }
    }

    public static boolean handleUserInput(Scanner scanner, boolean isRunning)
    {
        if (scanner.hasNextInt())
        {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println(GREEN + BOLD + "\n>> Navigating to Manage Projects..." + RESET);
                    ConsoleMenu.displayProjectHeader();
                    ConsoleMenu.displayProjectMenu();
                    ProjectService.handleProjectUserInput(isRunning, scanner);
                    break;
                case 2:
                    System.out.println(GREEN + BOLD + "\n>> Navigating to Manage Tasks..." + RESET);
                    ConsoleMenu.displayTaskHeader();
                    ConsoleMenu.displayTaskMenu();
                    TaskService.handleTaskUserInput(isRunning, scanner);
                    break;
                case 3:
                    System.out.println(GREEN + BOLD + "\n>> Navigating to View Status Reports..." + RESET);
                    try {
                        ReportService.generateStatusReport();
                    } catch (EmptyProjectListException e) {
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    }
                    System.out.print(ConsoleColors.BOLD + ConsoleColors.CYAN + "\n\n>> Press Enter to continue... " + ConsoleColors.RESET);
                    scanner.nextLine();
                    ConsoleMenu.displayHeader();
                    ConsoleMenu.displayMainMenu();
                    isRunning = ProjectService.handleUserInput(scanner, isRunning);
                    break;
                case 4:
                    isRunning = false;
                    System.out.println(GREEN + BOLD + "\nThank you using Project Management today!!\n" + RESET);
                    break;
                default:
                    System.out.println(RED + BOLD + "\n>> Invalid input. Please enter a number between 1 - 4" + RESET);
            }
        } else {
            System.out.println(RED + BOLD + "\n>> Invalid input, Please enter a number" + RESET);
            scanner.next();
        }
        return isRunning;
    }

}

