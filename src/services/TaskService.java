package services;

import java.util.Scanner;
import models.Task;
import models.Task.Status;
import models.User;
import utils.ConsoleMenu;
import utils.exceptions.EmptyProjectListException;
import utils.exceptions.InvalidTaskStatusException;
import utils.exceptions.ProjectNotFoundException;
import utils.exceptions.TaskFullException;
import utils.exceptions.TaskNotFoundException;
import utils.ConsoleColors;
import static utils.ConsoleColors.*;

/**
 * Service layer for task creation, updates, deletion, listing, and reporting.
 * Manages in-memory task storage and menu-driven task flows with validation.
 */
public class TaskService {
    
    // In-memory storage for tasks (fixed capacity for this exercise)
    private static final int MAX_TASKS = 50;
    private static Task[] tasks = new Task[MAX_TASKS]; 
    private static int taskCount = 0;

    

    /**
     * Store a task in the in-memory array or throw if capacity is reached.
     * @param task task to store
     * @throws TaskFullException when capacity is exceeded
     */
    public static void addTaskToStorage(Task task) throws TaskFullException {
        if (taskCount >= MAX_TASKS) {
            throw new TaskFullException(task.getName());
        } 
            tasks[taskCount] = task;
            taskCount++; 
    }

    /**
     * Check whether a task with the given name exists for a project.
     * @param projectId project identifier
     * @param name task name
     * @return true if found
     * @throws TaskNotFoundException when not found
     */
    public static boolean taskExists(String projectId, String name) throws TaskNotFoundException {
        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            if (t != null && t.getProjectId().equals(projectId) && t.getName().equals(name)) {
                return true;
            }
        }
        throw new TaskNotFoundException("Task does not exist");
    }

     /**
      * Check whether a task id exists.
      * @param taskId id to check
      * @return true if found
      */
     public static boolean taskExists(String taskId) {
        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            if (t != null && t.getId().equals(taskId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Display tasks for a project and route to the task submenu; keeps project context.
     * @param projectId project identifier
     * @param scanner shared scanner
     * @param isRunning app running flag
     * @throws TaskNotFoundException when no tasks are found
     */
    public static void filterByProject(String projectId, Scanner scanner, Boolean isRunning) throws TaskNotFoundException {
        filterByProject(projectId, scanner, isRunning, null);
    }

    // Overload that keeps project context for task submenu
    public static void filterByProject(String projectId, Scanner scanner, Boolean isRunning, String currentProjectId) throws TaskNotFoundException {
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
        TaskService.handleProjectTaskUserInput(isRunning, scanner, currentProjectId != null ? currentProjectId : projectId);      
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

    /**
     * Interactive flow to add a task: validates project, name rules, and status.
     * @param scanner shared scanner
     * @param projectId optional project id (if already chosen)
     * @param isRunning app running flag
     * @throws ProjectNotFoundException when project does not exist
     */
    public static void addTask(Scanner scanner, String projectId, Boolean isRunning) 
        throws ProjectNotFoundException {
    
        String assignedProjectId = "";
        if (projectId != null) {
            assignedProjectId = projectId;
        } else {
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + 
                            "Enter assigned project id: " + ConsoleColors.RESET);
            assignedProjectId = scanner.nextLine().trim();
        }

        // Validate project existence (propagate if not found so caller can re-prompt project)
        ProjectService.projectExists(assignedProjectId);

        // Get a valid task name (non-empty, non-numeric-only)
        String taskName = "";
        while (true) {
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + 
                            "Enter task name: " + ConsoleColors.RESET);
            taskName = scanner.nextLine();
            if (taskName == null || taskName.trim().isEmpty()) {
                System.out.println(ConsoleColors.RED + "ERROR: Task name cannot be empty" + ConsoleColors.RESET);
                continue;
            }
            String trimmedName = taskName.trim();
            if (trimmedName.matches("\\d+")) {
                System.out.println(ConsoleColors.RED + "ERROR: Task name cannot be numeric only" + ConsoleColors.RESET);
                continue;
            }
            // Task name must not duplicate an existing task under the same project
            try {
                if (TaskService.taskExists(assignedProjectId, trimmedName)) {
                    System.out.println(ConsoleColors.RED + "ERROR: Task already exists for this project" + ConsoleColors.RESET);
                    continue;
                }
            } catch (TaskNotFoundException e) {
                // task does not exist; proceed
            }
            break;
        }

        // Get a valid status, re-prompting on invalid entry
        Task createdTask = null;
        while (createdTask == null) {
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + 
                            "Enter initial status (Pending/In Progress/Completed): " + ConsoleColors.RESET);
            String statusInput = scanner.nextLine().trim();
            switch (statusInput) {
                case "Pending":
                    createdTask = Task.create(taskName, Status.PENDING, assignedProjectId);
                    break;
                case "In Progress":
                    createdTask = Task.create(taskName, Status.IN_PROGRESS, assignedProjectId);
                    break;
                case "Completed":
                    createdTask = Task.create(taskName, Status.COMPLETED, assignedProjectId);
                    break;
                default:
                    System.out.println(ConsoleColors.RED + "ERROR: Invalid status: '" + statusInput + 
                            "'. Must be one of: Pending, In Progress, Completed" + ConsoleColors.RESET);
            }
        }

        // Store task (may throw if storage is full)
        try {
            addTaskToStorage(createdTask);
            System.out.println(ConsoleColors.GREEN + ConsoleColors.BOLD + 
                            "\n\n>> Task '" + createdTask.getName() + 
                            "' added successfully to Project " + createdTask.getProjectId() + "!" + 
                            ConsoleColors.RESET);
        } catch (TaskFullException e) {
            System.out.println(ConsoleColors.RED + "ERROR: " + e.getMessage() + ConsoleColors.RESET);
        }

        pauseAndReturnToTaskMenu(scanner, isRunning);
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

    /**
     * Interactive status update flow: validates task id, then loops until a valid status is provided.
     * @param var0 shared scanner
     * @param isRunning app running flag
     * @param fromTask true when invoked from task submenu
     * @throws InvalidTaskStatusException when status text is invalid
     */
    public static void updateTask(Scanner var0, Boolean isRunning, boolean fromTask) throws InvalidTaskStatusException
   {
        String taskId = "";
        // Loop until a valid task id is provided
        while (true) {
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter task id: " + ConsoleColors.RESET);
            taskId = var0.nextLine();
            try {
                TaskService.getTask(taskId);
                break; // valid id
            } catch (TaskNotFoundException e) {
                System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
            }
        }

        boolean updated = false;
        while (!updated) {
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter new status (Pending/In Progress/Completed): " + ConsoleColors.RESET);
            String var3 = var0.nextLine();
            try {
                switch (var3) {
                case "Pending":
                        TaskService.updateTask(taskId, Status.PENDING);
                        updated = true;
                        break;
                case "In Progress":
                        TaskService.updateTask(taskId, Status.IN_PROGRESS);
                        updated = true;
                        break;
                case "Completed":
                        TaskService.updateTask(taskId, Status.COMPLETED);
                        updated = true;
                        break;
                default:
                    System.out.println(RED + "ERROR: Invalid status. Must be Pending, In Progress, or Completed." + RESET);
                }
            } catch (TaskNotFoundException e){
                System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                break; // task not found, exit loop
            }
        }
      if (updated) {
          try {
              Task t = TaskService.getTask(taskId);
              System.out.println(ConsoleColors.GREEN + ConsoleColors.BOLD + "\n\n>> Task " + "'" + ((Task)t).getName() + "'" + " marked as " + ((Task)t).getStatus() + "!\n" + ConsoleColors.RESET);
          } catch (TaskNotFoundException e) {
            System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
          }
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

        while (true) {
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter task id (or 0 to cancel): " + ConsoleColors.RESET);
            String var2 = var0.nextLine();
            if ("0".equals(var2)) {
                break;
            }
            boolean isDeleted = TaskService.removeTask(var2);
            if (isDeleted) {
                System.out.println(ConsoleColors.RED + ConsoleColors.BOLD + "\n\n>> Task deleted successfully!\n" + ConsoleColors.RESET);
                break;
            } else {
                System.out.println(ConsoleColors.RED + ConsoleColors.BOLD + "\nTask doesn't exist, so can't be deleted" + ConsoleColors.RESET);
            }
        }
        pauseAndReturn(var0, isRunning, fromTask);
   }

   /**
    * Handle the task submenu, optionally scoped to a current project.
    * @param var0 app running flag
    * @param var1 shared scanner
    * @param currentProjectId optional project context to reuse
    */
   public static void handleProjectTaskUserInput(Boolean var0, Scanner var1) {
      handleProjectTaskUserInput(var0, var1, null);
   }

   public static void handleProjectTaskUserInput(Boolean var0, Scanner var1, String currentProjectId) {
      String projectId = "";
      if (var1.hasNextInt()) {
         int var2 = var1.nextInt();
         var1.nextLine();
         switch (var2) {
            case 1:
               ConsoleMenu.displayTaskAddHeader();
               // If we are in a project context, reuse that project ID
               if (currentProjectId != null) {
                   try {
                       ProjectService.projectExists(currentProjectId);
                       addTask(var1, currentProjectId, var0);
                       handleProjectTaskUserInput(var0, var1, currentProjectId);
                       break;
                   } catch (ProjectNotFoundException e){
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                        // fall through to generic flow to re-prompt
                   }
               }
               ProjectService.displayProjects(var1, var0, true);
               while (true) {
                   System.out.print(BOLD + YELLOW + "Enter project ID to add task to (or 0 to return): " + RESET);
                   projectId = var1.nextLine();
                   if ("0".equals(projectId)) {
                       handleProjectTaskUserInput(var0, var1, null);
                       break;
                   }
                   try {
                       ProjectService.projectExists(projectId);
                       addTask(var1, projectId, var0);
                       handleProjectTaskUserInput(var0, var1, null);
                       break; // success
                   } catch (ProjectNotFoundException e){
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                   }
               }
               break;
            case 2:
                ConsoleMenu.displayTaskUpdateHeader();
                try {
                    updateTask(var1, var0, false);
                } catch (Exception e) {
                    System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                }
                handleProjectTaskUserInput(var0, var1, currentProjectId);
               break;
            case 3:
                ConsoleMenu.displayTaskRemoveHeader();
                removeTask(var1, var0, false);
                handleProjectTaskUserInput(var0, var1, currentProjectId);              
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

   /**
    * Entry point for the top-level task menu (non-scoped).
    * @param var0 app running flag
    * @param var1 shared scanner
    */
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
               while (true) {
                   System.out.print(BOLD + YELLOW + "Enter project ID to add task to (or 0 to return): " + RESET);
                   projectId = var1.nextLine();
                   if ("0".equals(projectId)) {
                       handleProjectTaskUserInput(var0, var1);
                       break;
                   }
                   try {
                       ProjectService.projectExists(projectId);
                       addTask(var1, projectId, var0);
                       handleProjectTaskUserInput(var0, var1);
                       break; // success
                   } catch (ProjectNotFoundException e){
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                   }
               }
               break;
            
            case 2:
                ProjectService.displayProjects(var1, var0, true);
                while (true) {
                    System.out.print(BOLD + YELLOW + "Enter project ID to view tasks (or 0 to return): " + RESET);
                    projectId = var1.nextLine();
                    if ("0".equals(projectId)) {
                        handleProjectTaskUserInput(var0, var1);
                        break;
                    }
                    try {
                        // Validate project existence first
                        ProjectService.projectExists(projectId);

                        TaskService.displayTasks(var1, var0, projectId, true);
                        System.out.print(BOLD + CYAN + "\n\n>> Press Enter to continue... " + RESET);
                        var1.nextLine();
                        ConsoleMenu.displayTaskHeader();
                        ConsoleMenu.displayTaskMenu();
                        TaskService.handleTaskUserInput(var0, var1);
                        handleProjectTaskUserInput(var0, var1);
                        break; // success
                    } catch (ProjectNotFoundException e) {
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    } catch (TaskNotFoundException e) {
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    }
                }
                break;

            case 3:
                ProjectService.displayProjects(var1, var0, true);
                while (true) {
                    System.out.print(BOLD + YELLOW + "Enter project ID to view tasks to update (or 0 to return): " + RESET);
                    projectId = var1.nextLine();
                    if ("0".equals(projectId)) {
                        handleProjectTaskUserInput(var0, var1);
                        break;
                    }
                    try {
                        ProjectService.projectExists(projectId);
                        TaskService.displayTasks(var1, var0, projectId, true);
                        ConsoleMenu.displayTaskUpdateHeader();
                        updateTask(var1, var0, true);
                        handleProjectTaskUserInput(var0, var1);
                        break; // success
                    } catch (ProjectNotFoundException e){
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    } catch (TaskNotFoundException e){
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    } catch (InvalidTaskStatusException e){
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    }
                }
               break;
            case 4:
                ProjectService.displayProjects(var1, var0, true);
                while (true) {
                    System.out.print(BOLD + YELLOW + "Enter project ID to view tasks to delete (or 0 to return): " + RESET);
                    projectId = var1.nextLine();
                    if ("0".equals(projectId)) {
                        handleProjectTaskUserInput(var0, var1);
                        break;
                    }
                    try {
                        ProjectService.projectExists(projectId);
                        TaskService.displayTasks(var1, var0, projectId, true);
                        ConsoleMenu.displayTaskRemoveHeader();
                        removeTask(var1, var0, true);
                        handleProjectTaskUserInput(var0, var1);
                        break; // success
                    } catch (ProjectNotFoundException e){
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    } catch (TaskNotFoundException e) {
                        System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
                    }
                }              
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

