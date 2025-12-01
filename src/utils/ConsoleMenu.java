package utils;

import services.ProjectService;

public class ConsoleMenu {

    public static void displayProjectHeader() {
        System.out.println("\n\n==================================");
        System.out.println("|\t PROJECT CATALOG \t|");
        System.out.println("==================================\n\n");
    }

    public static void displayProjectAddHeader() {
        System.out.println("\n\n==================================");
        System.out.println("|\t ADD NEW PROJECT \t|");
        System.out.println("==================================\n\n");
    }

    public static void displayTaskAddHeader() {
        System.out.println("\n\n==================================");
        System.out.println("|\t ADD NEW TASK \t\t|");
        System.out.println("==================================\n\n");
    }

    public static void displayTaskUpdateHeader() {
        System.out.println("\n\n==================================");
        System.out.println("|\t UPDATE TASK \t\t|");
        System.out.println("==================================\n\n");
    }
    
    public static void displayProjectMenu() {
        System.out.println("Filter Options:");
        System.out.println("-----------");
        System.out.println("1. Add Project");
        System.out.printf("2. View All Projects (%d)%n", ProjectService.getProjectCount());
        System.out.println("3. Software Projects Only");
        System.out.println("4. Hardware Projects Only");
        System.out.println("5. Search by Budget Range");
        System.out.print("\n\nEnter filter choice: ");
    }

    public static void displayProjectTaskMenu() {
        System.out.println("Options:");
        System.out.println("-----------");
        System.out.println("1. Add Task");
        System.out.println("2. Update Task Status");
        System.out.println("3. Remove Task");
        System.out.println("4. Back to Main Menu");
        System.out.print("\n\nEnter your choice: ");
    }
}

