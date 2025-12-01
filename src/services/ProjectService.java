package services;

import java.util.Scanner;
import models.Project;
import models.SoftwareProject;
import models.HardwareProject;
import static utils.ConsoleColors.*;
import utils.ConsoleMenu;
import utils.ValidationUtils;

public class ProjectService {
    
    //storage can take up to only 20 projects
    private static Project[] allProjects = new Project[20]; 
    private static int projectCount = 0;

    public static void addProjectToStorage(Project project) {
        if (projectCount < allProjects.length) {
            allProjects[projectCount] = project;
            projectCount++; 
        } else {
            System.out.println("Warning! Project list is full. Could not add Project " + project.getName());
        }
    }

    public static Project findProject(String id) {
        for (int i = 0; i < projectCount; i++) {
            Project p = allProjects[i];
            if (p != null && p.getId().equals(id)) {
                return p;
            }
        }   
        return null;
    }

    public static boolean projectExists(String projectID) {
        for (int i = 0; i < projectCount; i++) {
            Project p = allProjects[i];
            if (p != null && p.getId().equals(projectID)) {
                return true;
            }
        }
        return false;
    }

    public static int getProjectCount() {
        return projectCount;
    }

    public static void addProject(Scanner scanner) {
        System.out.print("Enter project name: ");
        String pName = scanner.nextLine();

        System.out.print("Enter project description: ");
        String pDescription = scanner.nextLine();
        
        System.out.print("Enter budget (numbers): ");
        long pBudget = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Enter team size (numbers): ");
        int pTeamSize = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter project type (Software or Hardware - type in full): ");
        String pProjectType = scanner.next();

        Project newProject = null;

        if (pProjectType.equals("Software")) {
            newProject = new SoftwareProject(pName, pDescription, pBudget, pTeamSize);
        } else if (pProjectType.equals("Hardware")) {
            newProject = new HardwareProject(pName, pDescription, pBudget, pTeamSize);
        } else {
            System.out.println(RED + "Invalid project type: " + pProjectType + ". Project not created.\n" + RESET);
        }

        if (newProject != null) {
            System.out.print(GREEN + "\nProject " + newProject.getName() + " created successfully!\n" + RESET);
        }

        scanner.nextLine();

        ConsoleMenu.displayProjectHeader();
        ConsoleMenu.displayProjectMenu();
    }

    public static void displayProjects(Scanner scanner, Boolean isRunning) {
        System.out.printf("All projects (%s)%n%n", projectCount);
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.println("|ID\t\t\t\t| PROJECT NAME\t| DESCRIPTION\t\t\t| TYPE\t| TEAM SIZE\t| BUDGET|");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < projectCount; i++) {
            Project p = allProjects[i];
            System.out.printf("|%s| %s| %s| %s| %d| %d|", p.getId(), p.getName(), p.getDescription(), p.getProjectType(), p.getTeamSize(), p.getBudget());
            System.out.println("\n-----------------------------------------------------------------------------------------------------------------\n");
        }

        displayProjectDetails(scanner, isRunning);
    }

    public static void displayProjectDetails(Scanner scanner, Boolean isRunning) {
        System.out.print("Enter project ID to view details (or 0 to return): ");
        
        if (scanner.hasNext()) {
            String choice = scanner.next();
            scanner.nextLine();

            switch (choice) {
                case "0":
                    ConsoleMenu.displayProjectHeader();
                    ConsoleMenu.displayProjectMenu();
                    break;
                default:
                    Project foundProject = findProject(choice);
                    if (foundProject != null) {
                        foundProject.getProjectDetails();
                        TaskService.filterByProject(foundProject.getId(), scanner, isRunning);
                    } else {
                        System.out.println("\nProject does not exist");
                    }                        
                    break;
            }
        }
        scanner.nextLine();
    }

    public static void filterByType(String projectType, Scanner scanner, Boolean isRunning) {
        if (!ValidationUtils.isValidProjectType(projectType)) {
            System.out.println("Invalid project type: " + projectType + "\n");
            return;
        }
                
        System.out.println("\n\n==================================");
        System.out.printf("|\t%s Projects\t|\n", projectType);
        System.out.println("==================================\n\n");

        for (int i = 0; i < projectCount; i++) {
            Project p = allProjects[i];
            if (p != null && projectType.equals(p.getProjectType())) {
                p.displayProject();
            }
        }

        displayProjectDetails(scanner, isRunning);
    }

    public static void filterByBudget(long minAmount, long maxAmount, Scanner scanner, Boolean isRunning) {
        if (!ValidationUtils.isValidBudgetRange(minAmount, maxAmount)) {
            return;
        }

        int count = 0;
        System.out.println("\n\n==================================");
        System.out.println("|\tProjects within range\t|");
        System.out.println("==================================\n\n");

        for (int i = 0; i < projectCount; i++) {
            Project p = allProjects[i]; 
            if (p != null && minAmount <= p.getBudget() && p.getBudget() <= maxAmount) {
                count++;
                p.displayProject();       
            }
        }

        if (count == 0) {
            System.out.println("No projects found within budget range\n\n");
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
                    addProject(scanner);
                    handleProjectUserInput(isRunning, scanner);
                    break;
                case 2:
                    displayProjects(scanner, isRunning);
                    break;
                case 3:
                    filterByType("Software", scanner, isRunning);
                    break;
                case 4:
                    filterByType("Hardware", scanner, isRunning);
                    break;
                case 5:
                    System.out.print("Enter mininum amount (numbers): ");
                    long min = scanner.nextInt();
                    System.out.print("Enter maximum amount (numbers): ");
                    long max = scanner.nextInt();

                    filterByBudget(min, max, scanner, isRunning);
                    break;
                default:
                    System.out.println("\n>> Invalid input. Please a number between 1 - 5");
            }
        } else {
            System.out.println("\n>> Invalid input, Please enter a number");
            scanner.next();
        }
        if (isRunning) {
            scanner.nextLine();
        }
    }
}

