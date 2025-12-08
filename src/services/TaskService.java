package services;

import java.util.Scanner;
import models.Task;
import models.Task.Status;
import models.User;
import utils.ConsoleMenu;
import utils.exceptions.EmptyProjectListException;
import utils.exceptions.InvalidTaskStatusException;
import utils.exceptions.TaskFullException;
import utils.exceptions.TaskNotFoundException;
import utils.ConsoleColors;
import static utils.ConsoleColors.*;

public class TaskService {
    
    // In-memory storage for tasks (fixed capacity for this exercise)
    private static final int MAX_TASKS = 50;
    private static Task[] tasks = new Task[MAX_TASKS]; 
    private static int taskCount = 0;

    

    public static void addTaskToStorage(Task task) throws TaskFullException {
        if (taskCount >= MAX_TASKS) {
            throw new TaskFullException(task.getName());
        } 
            tasks[taskCount] = task;
            taskCount++; 
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

    public static void filterByProject(String projectId, Scanner scanner, Boolean isRunning) throws TaskNotFoundException {
        int count = 0;
        System.out.println("Associated Tasks: \n");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("|ID\t\t\t\t\t| TASK NAME\t| STATUS\t|");
        System.out.println("----------------------------------------------------------------------------------");

        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            if (t != null && t.getProjectId().equals(projectId)) {
                count++;
                System.out.printf("|%s| \t%s| %s|", t.getId(), t.getName(), t.getStatus());
                System.out.println("\n------------------------------------------------------------------------------------\n");
            }
        }

        if (count == 0)
        {
            throw new TaskNotFoundException("No associated tasks found");
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

    public static Task getTask(String taskId) throws TaskNotFoundException
    {
        for (int i = 0; i < taskCount; i++)
        {
            Task t = tasks[i];
            if (t.getId().equals(taskId))
                return t;
        }
        throw new TaskNotFoundException("Task does not exist");
    }

    public static double completionRate(String projectID) throws EmptyProjectListException {
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
            throw new EmptyProjectListException("Project does not have any tasks");
        }
        return (((double) completed / total) * 100);
    }

    public static void addTask(Scanner var0, String projectId, Boolean isRunning) throws InvalidTaskStatusException {
      System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter task name: " + ConsoleColors.RESET);
      String var1 = var0.nextLine();
      String var2 = "";
      if (projectId != null)
      {
        var2 = projectId;
      } else {
          System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter assigned project id: " + ConsoleColors.RESET);
          var2 = var0.nextLine();
      }

      if (!ProjectService.projectExists(var2)) {
         System.out.println(ConsoleColors.RED + ConsoleColors.BOLD + "\n\n>> Project does not exist. Task not created.\n" + ConsoleColors.RESET);
         pauseAndReturnToTaskMenu(var0, isRunning);
         return;
      }
      System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter initial status (Pending/In Progress/Completed): " + ConsoleColors.RESET);
      String var3 = var0.nextLine();
      Object var4 = null;
      switch (var3) {
        case "Pending":
                var4 = Task.create(var1, Status.PENDING , var2);
            break;
        case "In Progress":
                var4 = Task.create(var1, Status.IN_PROGRESS , var2);
                break;
        case "Completed":
            var4 = Task.create(var1, Status.COMPLETED , var2);
            break;
        default:
            throw new InvalidTaskStatusException(var4);
      }
      if (var4 != null) {
         System.out.println(ConsoleColors.GREEN + ConsoleColors.BOLD + "\n\n>> Task '" + ((Task)var4).getName() + "'" + " added successfully to Project " + ((Task)var4).getProjectId() + "!" + ConsoleColors.RESET);
      }

      pauseAndReturnToTaskMenu(var0, isRunning);
   }

   private static void pauseAndReturnToTaskMenu(Scanner scanner, Boolean isRunning) {
      System.out.print(ConsoleColors.BOLD + ConsoleColors.CYAN + "\n\n>> Press Enter to continue... " + ConsoleColors.RESET);
      scanner.nextLine();
      ConsoleMenu.displayTaskHeader();
      ConsoleMenu.displayTaskMenu();
      TaskService.handleTaskUserInput(isRunning, scanner);
   }

   public static boolean updateTask(String taskId, Status status) throws TaskNotFoundException
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
    throw new TaskNotFoundException("Task does not exist, cannot update");
}


   public static boolean removeTask(String taskId)
   {
        int index = -1;
        for (int i = 0; i < taskCount; i++)
        {
            Task t = tasks[i];
            if (t.getId().equals(taskId))
            {  index = i;
                break;
            }
        }
        if (index == -1)
            return false;

        //shift all elements by 1
        for (int j = index; j < taskCount - 1; j++)
        {
            tasks[j] = tasks[j + 1];

        }
        tasks[taskCount - 1] = null;
        return true; 
   }

   public static void updateTask(Scanner var0, Boolean isRunning, boolean fromTask) throws InvalidTaskStatusException
   {
        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter task id: " + ConsoleColors.RESET);
        String var2 = var0.nextLine();
        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter new status (Pending/In Progress/Completed): " + ConsoleColors.RESET);
        String var3 = var0.nextLine();
        switch (var3) {
        case "Pending":
                try {
                    TaskService.updateTask(var2, Status.PENDING);
                } catch (TaskNotFoundException e){
                    System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                }
                break;
        case "In Progress":
                try {
                    TaskService.updateTask(var2, Status.IN_PROGRESS);
                } catch (TaskNotFoundException e){
                    System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                }
                break;
        case "Completed":
                try {
                    TaskService.updateTask(var2, Status.COMPLETED);
                } catch (TaskNotFoundException e) {
                    System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                }
            break;
        default:
            throw new InvalidTaskStatusException(var3);
      }
      try {
          Task t = TaskService.getTask(var2);
          System.out.println(ConsoleColors.GREEN + ConsoleColors.BOLD + "\n\n>> Task " + "'" + ((Task)t).getName() + "'" + " marked as " + ((Task)t).getStatus() + "!\n" + ConsoleColors.RESET);
      } catch (TaskNotFoundException e) {
        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
      }
      pauseAndReturn(var0, isRunning, fromTask);
   }

   private static void pauseAndReturn(Scanner scanner, Boolean isRunning, boolean fromTask) {
      System.out.print(ConsoleColors.BOLD + ConsoleColors.CYAN + "\n\n>> Press Enter to continue... " + ConsoleColors.RESET);
      scanner.nextLine();
      if (!fromTask)
      {
          ConsoleMenu.displayProjectTaskMenu();
          TaskService.handleProjectTaskUserInput(isRunning, scanner);
      } else {
        ConsoleMenu.displayTaskHeader();
        ConsoleMenu.displayTaskMenu();
        TaskService.handleTaskUserInput(isRunning, scanner);
      }
   }

   public static void displayTasks(Scanner scanner, Boolean isRunning, String projectId, Boolean fromTask) throws TaskNotFoundException
   {
    int count = 0;
    System.out.printf(GREEN + "\nAll tasks under project: %s%n%n" + RESET, projectId);
    System.out.println("----------------------------------------------------------------------------------");
    System.out.println("|ID\t\t\t\t\t| TASK NAME\t| STATUS\t|");
    System.out.println("----------------------------------------------------------------------------------");


    for (int i = 0; i < taskCount; i++) {
        Task t = tasks[i];
        if (t != null && t.getProjectId().equals(projectId)) {
            count++;
            System.out.printf("|%s| \t%s| %s|", t.getId(), t.getName(), t.getStatus());
            System.out.println("\n------------------------------------------------------------------------------------\n");
        }
    }   
    if (count == 0)
    {   
        throw new TaskNotFoundException("No tasks found for this project");        

    } else {
        double rate = completionRate(projectId);
        System.out.printf("Completion Rate: %.1f%%\n\n", rate);

    }
    if (!fromTask)
    {
        System.out.print(BOLD + CYAN + "\n\n>> Press Enter to continue... " + RESET);
        scanner.nextLine();
        ConsoleMenu.displayTaskHeader();
        ConsoleMenu.displayTaskMenu();
        TaskService.handleTaskUserInput(isRunning, scanner);
    }

   }


   public static void removeTask(Scanner var0, boolean isRunning, boolean fromTask)
   {
        User current = AuthService.getCurrentUser();
        if (current == null || !current.canDeleteTasks()) {
            System.out.println(ConsoleColors.RED + ConsoleColors.BOLD + "\nYou do not have permission to delete tasks.\n" + ConsoleColors.RESET);
            pauseAndReturn(var0, isRunning, fromTask);
            return;
        }

        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter task id: " + ConsoleColors.RESET);
        String var2 = var0.nextLine();
        boolean isDeleted = TaskService.removeTask(var2);
        if (isDeleted) {
            System.out.println(ConsoleColors.RED + ConsoleColors.BOLD + "\n\n>> Task deleted successfully!\n" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + ConsoleColors.BOLD + "\nTask doesn't exist, so can't be deleted" + ConsoleColors.RESET);
        }
        pauseAndReturn(var0, isRunning, fromTask);
   }

    public static void handleProjectTaskUserInput(Boolean var0, Scanner var1) {
      if (var1.hasNextInt()) {
         int var2 = var1.nextInt();
         var1.nextLine();
         switch (var2) {
            case 1:
               ConsoleMenu.displayTaskAddHeader();
               try {
                   addTask(var1, null, var0);
               } catch (Exception e){
                    System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
               }
               handleProjectTaskUserInput(var0, var1);
               break;
            case 2:
                ConsoleMenu.displayTaskUpdateHeader();
                try {
                    updateTask(var1, var0, false);
                } catch (Exception e) {
                    System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                }
                handleProjectTaskUserInput(var0, var1);
               break;
            case 3:
                ConsoleMenu.displayTaskRemoveHeader();
                removeTask(var1, var0, false);
                handleProjectTaskUserInput(var0, var1);              
                break;
            case 4:
                ConsoleMenu.displayHeader();
                ConsoleMenu.displayMainMenu();
                ProjectService.handleUserInput(var1, var0);
               break;
            default:
               System.out.println(ConsoleColors.RED + ConsoleColors.BOLD + "\n>> Invalid input. Please enter a number between 1 - 4" + ConsoleColors.RESET);
         }
      } else {
         System.out.println(ConsoleColors.RED + ConsoleColors.BOLD + "\n>> Invalid input, Please enter a number" + ConsoleColors.RESET);
         var1.next();
      }

      if (var0) {
         var1.nextLine();
      }

   }

   public static void handleTaskUserInput(Boolean var0, Scanner var1)
   {
        String projectId = "";
        if (var1.hasNextInt()) {
         int var2 = var1.nextInt();
         var1.nextLine();
         switch (var2) {
            case 1:
               ConsoleMenu.displayTaskAddHeader();
               ProjectService.displayProjects(var1, var0, true);
               System.out.print(BOLD + YELLOW + "Enter project ID to add task to (or 0 to return): " + RESET);
               try {
                   projectId = var1.nextLine();
                   addTask(var1, projectId, var0);
               } catch (InvalidTaskStatusException e){
                    System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
               }
               handleProjectTaskUserInput(var0, var1);
               break;
            
            case 2:
                ProjectService.displayProjects(var1, var0, true);
                System.out.print(BOLD + YELLOW + "Enter project ID to view tasks (or 0 to return): " + RESET);
                try {
                projectId = var1.nextLine();
                    TaskService.displayTasks(var1, var0, projectId, true);
                } catch (TaskNotFoundException e) {
                    System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                }
                System.out.print(BOLD + CYAN + "\n\n>> Press Enter to continue... " + RESET);
                var1.nextLine();
                ConsoleMenu.displayTaskHeader();
                ConsoleMenu.displayTaskMenu();
                TaskService.handleTaskUserInput(var0, var1);
                handleProjectTaskUserInput(var0, var1);
                break;

            case 3:
                ProjectService.displayProjects(var1, var0, true);
                System.out.print(BOLD + YELLOW + "Enter project ID to view tasks to update (or 0 to return): " + RESET);
                try {
                projectId = var1.nextLine();
                    TaskService.displayTasks(var1, var0, projectId, true);
                    ConsoleMenu.displayTaskUpdateHeader();
                    updateTask(var1, var0, true);
                } catch (TaskNotFoundException e){
                    System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                } catch (InvalidTaskStatusException e){
                    System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                }
                handleProjectTaskUserInput(var0, var1);
               break;
            case 4:
                ProjectService.displayProjects(var1, var0, true);
                System.out.print(BOLD + YELLOW + "Enter project ID to view tasks to delete (or 0 to return): " + RESET);
                try {
                projectId = var1.nextLine();
                    TaskService.displayTasks(var1, var0, projectId, true);
                    ConsoleMenu.displayTaskRemoveHeader();
                    removeTask(var1, var0, true);
                } catch (TaskNotFoundException e) {
                    System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                }
                handleProjectTaskUserInput(var0, var1);              
                break;
            case 5:
                ConsoleMenu.displayHeader();
                ConsoleMenu.displayMainMenu();
                ProjectService.handleUserInput(var1, var0);
               break;
            default:
               System.out.println(ConsoleColors.RED + ConsoleColors.BOLD + "\n>> Invalid input. Please enter a number between 1 - 5" + ConsoleColors.RESET);
         }
      } else {
         System.out.println(ConsoleColors.RED + ConsoleColors.BOLD + "\n>> Invalid input, Please enter a number" + ConsoleColors.RESET);
         var1.next();
      }

      if (var0) {
         var1.nextLine();
      }
   }

   public static float[] getTasksReport(String projectId)
   {
        float total = 0.0f;
        float completed = 0.0f;
        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            if (t != null && t.getProjectId().equals(projectId)) {                
                total++;
                if (t.getStatus().equals(Task.Status.COMPLETED)) {
                    completed++;
                }
            }
        }
        float progress =  (((float) completed / total) * 100);
        return new float[] {total, completed, progress};
   }
}

