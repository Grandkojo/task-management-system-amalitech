package services;

import models.SoftwareProject;
import models.Task;
import models.Task.Status;
import utils.ConsoleColors;
import static utils.ConsoleColors.*;
import utils.ConsoleMenu;
import utils.exceptions.TaskNotFoundException;

import java.util.Scanner;

/**
 * Demonstrates concurrent task updates using multiple threads.
 */
public final class ConcurrencyService {

    private ConcurrencyService() {
    }

    /**
     * Simulate three threads updating tasks concurrently in a safe way.
     * @param scanner shared scanner for user prompts
     * @param isRunning app running flag
     * @param fromTask true when invoked from the task menu so we can return there
     * @throws InterruptedException if any thread is interrupted while joining
     */
    public static void simulateConcurrentTasksUpdates(Scanner scanner, Boolean isRunning, Boolean fromTask)
            throws InterruptedException {

        SoftwareProject softwareProject = new SoftwareProject(
                "Project to test",
                "This project is used to task parallel updates",
                1200,
                2
        );
        Task task1 = Task.create("Start thinking", Status.PENDING, softwareProject.getId());
        Task task2 = Task.create("Formulate plans", Status.IN_PROGRESS, softwareProject.getId());
        Task task3 = Task.create("Take action", Status.PENDING, softwareProject.getId());
        TaskService.addTaskToStorage(task1);
        TaskService.addTaskToStorage(task2);
        TaskService.addTaskToStorage(task3);

        Thread thread1 = new Thread(() -> {
            try {
                TaskService.updateTask(task1.getId(), Status.IN_PROGRESS);
                System.out.printf("\n%s updating %s -> %s\n", "Thread-1",
                        task1.getId(), TaskService.getTask(task1.getId()).getStatus());
            } catch (TaskNotFoundException e) {
                System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                TaskService.updateTask(task2.getId(), Status.COMPLETED);
                System.out.printf("%s updating %s -> %s\n", "Thread-2",
                        task2.getId(), TaskService.getTask(task2.getId()).getStatus());
            } catch (TaskNotFoundException e) {
                System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
            }
        });

        Thread thread3 = new Thread(() -> {
            try {
                TaskService.updateTask(task3.getId(), Status.IN_PROGRESS);
                System.out.printf("%s updating %s -> %s\n", "Thread-3",
                        task3.getId(), TaskService.getTask(task3.getId()).getStatus());
            } catch (TaskNotFoundException e) {
                System.out.println(RED + "ERROR: " + e.getMessage() + RESET);
            }
        });

        System.out.print(ConsoleColors.BOLD + ConsoleColors.CYAN + "\n>> Press Enter to start concurrent updates... " + ConsoleColors.RESET);
        scanner.nextLine();
        System.out.println("Starting 3 threads...");

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("All threads finished successfully");
        System.out.println("Tasks updates applied concurrently and safely");

        if (fromTask) {
            System.out.print(BOLD + CYAN + "\n\n>> Press Enter to continue... " + RESET);
            scanner.nextLine();
            ConsoleMenu.displayTaskHeader();
            ConsoleMenu.displayTaskMenu();
            TaskService.handleTaskUserInput(isRunning, scanner);
        }
    }
}


