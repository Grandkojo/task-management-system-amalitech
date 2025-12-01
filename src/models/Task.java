package models;

import java.util.UUID;
import services.ProjectService;
import services.TaskService;
import interfaces.Completable;

public class Task implements Completable {

    private String id;
    private String projectId;
    private String name;
    private Status status;

    public static enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }

    public Task(String name, Status status, String projectId) {
        if (ProjectService.projectExists(projectId)) {
            if (!TaskService.taskExistsForProject(projectId, name)) {

                this.projectId = projectId;
                this.id = UUID.randomUUID().toString();
                this.name = name;
                this.status = status;

                // Add task to storage via service
                TaskService.addTaskToStorage(this);
            } else {
                System.out.println("\nTask already exists for project\n");
            }
        } else {
            System.out.println("Project does not exist");
        }
    }

    public boolean isCompleted() {
        if (this.status.equals(Status.COMPLETED)) {
            return true;
        }
        return false;
    }

    public Task getTask(String taskId)
    {
        return TaskService.getTask(taskId);
    }

    public String getId() {
        return this.id;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public String getName() {
        return this.name;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public boolean updateTask(String taskId, Status status)
    {
        if (TaskService.taskExists(taskId))
        {
            return TaskService.updateTask(taskId, status);
        }
        return false;
    }

       public boolean removeTask(String taskId)
    {
        if (TaskService.taskExists(taskId))
        {
            return TaskService.removeTask(taskId);
        }
        return false;
    }
}
