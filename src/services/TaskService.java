package services;

import java.util.Scanner;
import models.Task;
import models.Task.Status;
import models.HardwareProject;
import models.Project;
import models.SoftwareProject;
import utils.ConsoleMenu;
import utils.ConsoleColors;

public class TaskService {
    
    private static Task[] tasks = new Task[50]; 
    private static int taskCount = 0;

    public static void addTaskToStorage(Task task) {
        if (taskCount < tasks.length) {
            tasks[taskCount] = task;
            taskCount++; 
        } else {
            System.out.println("Warning! Task list is full. Could not add Task " + task.getName());
        }
    }

    public static boolean taskExistsForProject(String projectId, String name) {
        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            if (t != null && t.getProjectId().equals(projectId) && t.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

     public static boolean taskExists(String taskId) {
        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            if (t != null && t.getId().equals(taskId)) {
                return true;
            }
        }
        return false;
    }

    public static void filterByProject(String projectId, Scanner scanner, Boolean isRunning) {
        System.out.println("Associated Tasks: \n");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("|ID\t\t\t\t\t| TASK NAME\t| STATUS\t|");
        System.out.println("----------------------------------------------------------------------------------");

        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            if (t != null && t.getProjectId().equals(projectId)) {
                System.out.printf("|%s| \t%s| %s|", t.getId(), t.getName(), t.getStatus());
                System.out.println("\n------------------------------------------------------------------------------------\n");
            }
        }

        double rate = completionRate(projectId);
        System.out.printf("Completion Rate: %.1f%%\n\n", rate);
        
        ConsoleMenu.displayProjectTaskMenu();  
        TaskService.handleProjectTaskUserInput(isRunning, scanner);      
    }

    public static void getTasks() {
        System.out.println("All Tasks: \n");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("|ID\t\t\t\t| TASK NAME\t| STATUS\t\t\t|");
        System.out.println("------------------------------------------------------------------------------");

        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            if (t != null) {
                System.out.printf("|%s| %s| %s|", t.getId(), t.getName(), t.getStatus());
                System.out.println("\n---------------------------------------------------------------------------\n");
            }
        }
    }

    public static Task getTask(String taskId)
    {
        for (int i = 0; i < taskCount; i++)
        {
            Task t = tasks[i];
            if (t.getId().equals(taskId))
                return t;
        }
        return null;
    }

    public static double completionRate(String projectID) {
        int total = 0;
        int completed = 0;
        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            if (t != null && t.getProjectId().equals(projectID)) {
                total++;
                if (t.getStatus().equals(Task.Status.COMPLETED)) {
                    completed++;
                }
            }
        }
        if (total == 0) {
            return 0.0;
        }
        return (((double) completed / total) * 100);
    }

    public static void addTask(Scanner var0) {
      System.out.print("Enter task name: ");
      String var1 = var0.nextLine();
      System.out.print("Enter assigned project id: ");
      String var2 = var0.nextLine();
      System.out.print("Enter initial status (Pending/In Progress/Completed): ");
      String var3 = var0.nextLine();
      Object var4 = null;
      switch (var3) {
        case "Pending":
                var4 = new Task(var1, Status.PENDING , var2);
            break;
        case "In Progress":
                var4 = new Task(var1, Status.IN_PROGRESS , var2);
                break;
        case "Completed":
            var4 = new Task(var1, Status.COMPLETED , var2);
            break;
        default:
            System.out.println(ConsoleColors.RED + "Status not allowed, allowed ones are (Pending/In Progress/Completed)");
      }
      if (var4 != null) {
         System.out.println(ConsoleColors.GREEN+"Task " + "" + ((Task)var4).getName() + "" + " added successfully to Project " + ((Task)var4).getProjectId() + "!" + ConsoleColors.RESET);
      }

      var0.nextLine();
      ConsoleMenu.displayProjectHeader();
      ConsoleMenu.displayProjectMenu();
   }

   public static boolean updateTask(String taskId, Status status)
{
    for (int i = 0; i < taskCount; i++)
    {
        Task t = tasks[i];
        
        if (t.getId().equals(taskId)) 
        {
            t.setStatus(status);
            return true; 
        }
    }
    return false;
}


   public static boolean removeTask(String taskId)
   {
        for (int i = 0; i < taskCount; i++)
        {
            Task t = tasks[i];
        }
        return false;
   }

   public static void updateTask(Scanner var0, Boolean isRunning)
   {
        System.out.print("Enter task id: ");
        String var2 = var0.nextLine();
        System.out.print("Enter new status (Pending/In Progress/Completed): ");
        String var3 = var0.nextLine();
        Boolean var4 = null;
        switch (var3) {
        case "Pending":
               var4 = TaskService.updateTask(var3, Status.PENDING);
                break;
        case "In Progress":
               var4 = TaskService.updateTask(var3, Status.IN_PROGRESS);
                break;
        case "Completed":
               var4 = TaskService.updateTask(var3, Status.COMPLETED);
            break;
        default:
            System.out.println(ConsoleColors.RED + "Status not allowed, allowed ones are (Pending/In Progress/Completed)");
      }
      
      Task t = TaskService.getTask(var2);
      if (t != null) {
         System.out.println(ConsoleColors.GREEN+"Task " + "" + ((Task)t).getName() + "" + " marked as " + ((Task)t).getStatus() + "!\n" + ConsoleColors.RESET);
      }

      ConsoleMenu.displayProjectHeader();
      ConsoleMenu.displayProjectMenu();
        
   }

    public static void handleProjectTaskUserInput(Boolean var0, Scanner var1) {
      if (var1.hasNextInt()) {
         int var2 = var1.nextInt();
         var1.nextLine();
         switch (var2) {
            case 1:
               ConsoleMenu.displayTaskAddHeader();
               addTask(var1);
               handleProjectTaskUserInput(var0, var1);
               break;
            case 2:
                ConsoleMenu.displayTaskUpdateHeader();
                updateTask(var1, var0);
                handleProjectTaskUserInput(var0, var1);

               break;
            case 3:
            //    filterByType("Software", var1);
               break;
            case 4:
            //    filterByType("Hardware", var1);
               break;
            case 5:
            //    System.out.print("Enter mininum amount (numbers): ");
            //    long var3 = (long)var1.nextInt();
            //    System.out.print("Enter maximum amount (numbers): ");
            //    long var5 = (long)var1.nextInt();
            //    filterByBudget(var3, var5, var1);
               break;
            default:
               System.out.println("\n>> Invalid input. Please a number between 1 - 4");
         }
      } else {
         System.out.println("\n>> Invalid input, Please enter a number");
         var1.next();
      }

      if (var0) {
         var1.nextLine();
      }

   }
}

