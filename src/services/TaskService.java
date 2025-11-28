package services;

import java.util.Scanner;
import models.Task;
import models.Project;
import utils.ConsoleMenu;

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

    public static void filterByProject(String projectId) {
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
}

