package utils;

import services.ProjectService;
import static utils.ConsoleColors.*;

public class ConsoleMenu {

     public static void displayHeader() {
        System.out.println(CYAN + BOLD + "\n\n==================================");
        System.out.println("| JAVA PROJECT MANAGEMENT SYSTEM |");
        System.out.println("==================================" + RESET + "\n\n");
    }

     public static void displayMainMenu() {
        System.out.println(BOLD + YELLOW + "Main Menu:" + RESET);
        System.out.println(YELLOW + "-----------" + RESET);
        System.out.println(WHITE + "1. Manage Projects" + RESET);
        System.out.println(WHITE + "2. Manage Tasks" + RESET);
        System.out.println(WHITE + "3. View Status Reports" + RESET);
        System.out.println(WHITE + "4. Exit Application" + RESET);
        System.out.print("\n\n" + BOLD + GREEN + ">> Enter your choice: " + RESET);
    }


    public static void displayProjectHeader() {
        System.out.println(CYAN + BOLD + "\n\n==================================");
        System.out.println("|\t PROJECT CATALOG \t|");
        System.out.println("==================================" + RESET + "\n\n");
    }

    public static void displayProjectAddHeader() {
        System.out.println(CYAN + BOLD + "\n\n==================================");
        System.out.println("|\t ADD NEW PROJECT \t|");
        System.out.println("==================================" + RESET + "\n\n");
    }

    public static void displayTaskHeader()
    {
        System.out.println(CYAN + BOLD + "\n\n==================================");
        System.out.println("|\t TASK CATALOG \t\t|");
        System.out.println("==================================" + RESET + "\n\n");   
    }

    public static void displayTaskMenu()
    {
        System.out.println(BOLD + YELLOW + "Task Options:" + RESET);
        System.out.println(YELLOW + "-----------" + RESET);
        System.out.println(WHITE + "1. Add Task To A Project" + RESET);
        System.out.println(WHITE + "2. View All Tasks Per Project" + RESET);
        System.out.println(WHITE + "3. Update Task In A Project" + RESET);
        System.out.println(WHITE + "4. Delete Task In A Project" + RESET);
        System.out.println(WHITE + "5. Back to Main Menu" + RESET);

        System.out.print("\n\n" + BOLD + GREEN + ">> Enter your choice: " + RESET);
    }

    public static void displayTaskAddHeader() {
        System.out.println(CYAN + BOLD + "\n\n==================================");
        System.out.println("|\t ADD NEW TASK \t\t|");
        System.out.println("==================================" + RESET + "\n\n");
    }

    public static void displayTaskUpdateHeader() {
        System.out.println(CYAN + BOLD + "\n\n==================================");
        System.out.println("|\t UPDATE TASK \t\t|");
        System.out.println("==================================" + RESET + "\n\n");
    }

    public static void displayTaskRemoveHeader()
    {
        System.out.println(CYAN + BOLD + "\n\n==================================");
        System.out.println("|\t REMOVE TASK \t\t|");
        System.out.println("==================================" + RESET + "\n\n");
    }
    
    public static void displayProjectMenu() {
        System.out.println(BOLD + YELLOW + "Filter Options:" + RESET);
        System.out.println(YELLOW + "-----------" + RESET);
        System.out.println(WHITE + "1. Add Project" + RESET);
        System.out.printf(WHITE + "2. View All Projects (%d)%n" + RESET, ProjectService.getProjectCount());
        System.out.println(WHITE + "3. Software Projects Only" + RESET);
        System.out.println(WHITE + "4. Hardware Projects Only" + RESET);
        System.out.println(WHITE + "5. Search by Budget Range" + RESET);
        System.out.print("\n\n" + BOLD + GREEN + ">> Enter filter choice: " + RESET);
    }

    public static void displayProjectTaskMenu() {
        System.out.println(BOLD + YELLOW + "Options:" + RESET);
        System.out.println(YELLOW + "-----------" + RESET);
        System.out.println(WHITE + "1. Add Task" + RESET);
        System.out.println(WHITE + "2. Update Task Status" + RESET);
        System.out.println(WHITE + "3. Remove Task" + RESET);
        System.out.println(WHITE + "4. Back to Main Menu" + RESET);
        System.out.print("\n\n" + BOLD + GREEN + ">> Enter your choice: " + RESET);
    }

}

