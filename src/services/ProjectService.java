package services;

import java.util.Scanner;
import models.Project;
import models.SoftwareProject;
import models.HardwareProject;
import models.User;
import static utils.ConsoleColors.*;

import utils.ConsoleColors;
import utils.ConsoleMenu;
import utils.ValidationUtils;
import utils.ExitHandler;
import utils.exceptions.EmptyProjectListException;
import utils.exceptions.InvalidBudgetRangeException;
import utils.exceptions.InvalidInputException;
import utils.exceptions.InvalidProjectTypeException;
import utils.exceptions.ProjectFullException;
import utils.exceptions.ProjectNotFoundException;
import utils.exceptions.TaskNotFoundException;

/**
 * Service layer for creating, storing, and presenting projects.
 * Handles menu-driven project interactions and bridges to task views.
 */
public class ProjectService {
    
    // In-memory storage for projects (fixed capacity for this exercise)
    private static final int MAX_PROJECTS = 20;  
    private static Project[] allProjects = new Project[MAX_PROJECTS]; 
    private static int projectCount = 0;

    

    /**
     * Persist a project into the in-memory store, enforcing capacity.
     * @param project project to store
     * @throws ProjectFullException when the storage array is full
     */
    public static void addProjectToStorage(Project project) throws ProjectFullException {
        if (projectCount >= MAX_PROJECTS)
            throw new ProjectFullException(project.getName());
        allProjects[projectCount] = project;
        projectCount++; 
    }

    private static void redisplayProjectMenu(Scanner scanner, Boolean isRunning) {
        ConsoleMenu.displayProjectHeader();
        ConsoleMenu.displayProjectMenu();
        handleProjectUserInput(isRunning, scanner);
    }

    private static void displayUserPanel(Scanner scanner) throws InvalidInputException {
        User[] users = AuthService.getAvailableUsers();
        if (users == null || users.length == 0) {
            throw new InvalidInputException("No users configured to switch");
        }

        System.out.println(CYAN + BOLD + "\n\n========================");
        System.out.println("|        User Panel     |");
        System.out.println("========================" + RESET + "\n");
        for (int i = 0; i < users.length; i++) {
            User u = users[i];
            System.out.printf("%d. %s (%s)%n", i + 1, u.getName(), u.getRole());
        }
        System.out.print("\n" + BOLD + GREEN + ">> Enter user number to switch: " + RESET);

        if (!scanner.hasNextInt()) {
            scanner.next();
            throw new InvalidInputException("Invalid input. Please enter a number");
        }

        int selection = scanner.nextInt();
        scanner.nextLine();
        if (selection < 1 || selection > users.length) {
            throw new InvalidInputException("Selection out of range");
        }

        User selected = users[selection - 1];
        AuthService.login(selected);
        System.out.println(GREEN + BOLD + "\n>> Switched to " + selected.getDisplayLabel() + RESET);
    }

    /**
     * Find a project by id or throw if not found.
     * @param id project identifier
     * @return the matching project
     * @throws ProjectNotFoundException when no project matches the id
     */
    public static Project findProject(String id) throws ProjectNotFoundException {
        for (int i = 0; i < projectCount; i++) {
            Project p = allProjects[i];
            if (p != null && p.getId().equals(id)) {
                return p;
            }
        }   
        throw new ProjectNotFoundException();
    }

    /**
     * Check existence of a project id or throw if not found.
     * @param projectID id to look up
     * @return true if found
     * @throws ProjectNotFoundException when not found
     */
    public static boolean projectExists(String projectID) throws ProjectNotFoundException {
        try {
            findProject(projectID);
            return true;
        } catch (ProjectNotFoundException e){
            throw new ProjectNotFoundException();
        }
    }

    /**
     * @return current number of stored projects.
     */
    public static int getProjectCount() {
        return projectCount;
    }

    /**
     * Interactive project creation flow with input validation and type enforcement.
     * @param scanner shared scanner for console input
     * @throws InvalidProjectTypeException when type is not Software/Hardware
     * @throws ProjectFullException when storage is full
     */
    public static void addProject(Scanner scanner) throws InvalidProjectTypeException, ProjectFullException {
        System.out.print(BOLD + YELLOW + "Enter project name: " + RESET);
        String pName = scanner.nextLine();

        System.out.print(BOLD + YELLOW + "Enter project description: " + RESET);
        String pDescription = scanner.nextLine();
        
        long pBudget;
        while (true) {
            System.out.print(BOLD + YELLOW + "Enter budget (numbers): " + RESET);
            if (!scanner.hasNextLong()) {
                System.out.println(RED + "ERROR: Please enter a numeric budget." + RESET);
                scanner.next(); // discard invalid token
                continue;
            }
            pBudget = scanner.nextLong();
            scanner.nextLine();
            break;
        }

        int pTeamSize;
        while (true) {
            System.out.print(BOLD + YELLOW + "Enter team size (numbers): " + RESET);
            if (!scanner.hasNextInt()) {
                System.out.println(RED + "ERROR: Please enter a numeric team size." + RESET);
                scanner.next(); // discard invalid token
                continue;
            }
            pTeamSize = scanner.nextInt();
            scanner.nextLine();
            break;
        }

        String pProjectType;
        while (true) {
            System.out.print(BOLD + YELLOW + "Enter project type (Software or Hardware - type in full): " + RESET);
            pProjectType = scanner.next();
            scanner.nextLine();
            if (ValidationUtils.isValidProjectType(pProjectType)) {
                break;
            }
            System.out.println(RED + "ERROR: Invalid project type. Please enter Software or Hardware." + RESET);
        }

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

    /**
     * Print a table of projects; optionally route to details lookup unless invoked from task flow.
     * @param scanner shared scanner
     * @param isRunning app running flag
     * @param fromTask true when invoked from task flows to avoid recursive prompts
     */
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

    /**
     * Interactive prompt to view project details; loops until a valid id or return is chosen.
     * @param scanner shared scanner
     * @param isRunning app running flag
     */
    public static void displayProjectDetails(Scanner scanner, Boolean isRunning) {
        while (true) {
            System.out.print(BOLD + YELLOW + "Enter project ID to view details (or 0 to return): " + RESET);

            if (!scanner.hasNext()) {
                return;
            }

            String choice = scanner.next();
            scanner.nextLine();

            if ("0".equals(choice)) {
                ConsoleMenu.displayProjectHeader();
                ConsoleMenu.displayProjectMenu();
                handleProjectUserInput(isRunning, scanner);
                return;
            }

            try {
                Project foundProject = findProject(choice);
                // Show tasks and stay in this project context for subsequent task actions
                TaskService.filterByProject(foundProject.getId(), scanner, isRunning, foundProject.getId());
                ConsoleMenu.getProjectDetails(foundProject);
            } catch (TaskNotFoundException e) {
                System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
            } catch (ProjectNotFoundException e){
                System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
            }
            // Loop continues, re-prompting for project ID
        }
    }

    /**
     * Filter projects by type; re-prompts on invalid type input.
     * @param projectType Software or Hardware
     * @param scanner shared scanner
     * @param isRunning app running flag
     * @throws InvalidProjectTypeException when type is invalid
     */
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

    /**
     * Filter projects by budget range; loops on invalid numeric input or invalid range.
     * @param minAmount minimum budget
     * @param maxAmount maximum budget
     * @param scanner shared scanner
     * @param isRunning app running flag
     * @throws InvalidBudgetRangeException when min/max are invalid
     */
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

    /**
     * Handle project submenu choices.
     * @param isRunning app running flag
     * @param scanner shared scanner
     */
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
                        redisplayProjectMenu(scanner, isRunning);
                    }
                    break;
                case 4:
                    try {
                        filterByType("Hardware", scanner, isRunning);
                    } catch (InvalidProjectTypeException e) {
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                        redisplayProjectMenu(scanner, isRunning);
                    }
                    break;
                case 5:
                    while (true) {
                        try {
                            System.out.print(BOLD + YELLOW + "Enter mininum amount (numbers): " + RESET);
                            if (!scanner.hasNextLong()) {
                                System.out.println(RED + "ERROR: Invalid input. Please enter numeric values only." + RESET);
                                scanner.next(); // discard invalid token
                                continue;
                            }
                            long min = scanner.nextLong();
                            scanner.nextLine();

                            System.out.print(BOLD + YELLOW + "Enter maximum amount (numbers): " + RESET);
                            if (!scanner.hasNextLong()) {
                                System.out.println(RED + "ERROR: Invalid input. Please enter numeric values only." + RESET);
                                scanner.next(); // discard invalid token
                                continue;
                            }
                            long max = scanner.nextLong();
                            scanner.nextLine();

                            filterByBudget(min, max, scanner, isRunning);
                            break; // success, exit loop to return to caller
                        } catch (InvalidBudgetRangeException e) {
                            System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                        }
                    }
                    break;
                default:
                    System.out.println(RED + BOLD + "\n>> Invalid input. Please enter a number between 1 - 5" + RESET);
            }
        } else {
            System.out.println(RED + BOLD + "\n>> Invalid input, Please enter a number" + RESET);
            scanner.next();
        }
    }

    /**
     * Handle main menu choices; returns false when the app should exit.
     * @param scanner shared scanner
     * @param isRunning current running flag
     * @return false when app should terminate
     */
    public static boolean handleUserInput(Scanner scanner, boolean isRunning)
    {
        // Gracefully handle EOF (e.g., Ctrl+D) by exiting the loop without errors
        if (!scanner.hasNext()) {
            ExitHandler.printOnce();
            return false;
        }

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
                    System.out.println(GREEN + BOLD + "\n>> Navigating to User Panel..." + RESET);
                    try {
                        displayUserPanel(scanner);
                    } catch (InvalidInputException e) {
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    }
                    ConsoleMenu.displayHeader();
                    ConsoleMenu.displayMainMenu();
                    isRunning = ProjectService.handleUserInput(scanner, isRunning);
                    break;
                case 5:
                    isRunning = false;
                    ExitHandler.printOnce();
                    return false; // stop immediately
                default:
                    System.out.println(RED + BOLD + "\n>> Invalid input. Please enter a number between 1 - 5" + RESET);
            }
        } else {
            System.out.println(RED + BOLD + "\n>> Invalid input, Please enter a number" + RESET);
            scanner.next();
        }
        return isRunning;
    }

}

