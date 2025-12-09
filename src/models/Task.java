package models;

import interfaces.Completable;

/**
 * Represents a task under a project. Implements Completable and
 * generates sequential IDs (TSK001, TSK002, ...) for each run.
 */
public class Task implements Completable {

    private String id;
    private String projectId;
    private String name;
    private Status status;

    private static int taskCount = 0;


    public static enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }

    private Task(String id, String name, Status status, String projectId){
        this.id = id;
        this.name = name;
        this.status = status;
        this.projectId = projectId;
    }

    public static Task create(String name, Status status, String projectId) {
        return new Task(generateTaskId(), name, status, projectId);
    }

    private static synchronized String generateTaskId() {
        taskCount++;
        return String.format("TSK%03d", taskCount);
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
    /**
     * Check if the task is completed.
     * @return true if the task is completed, false otherwise.
    */
    @Override
    public boolean isCompleted() {
        if (this.status.equals(Status.COMPLETED)) {
            return true;
        }
        return false;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }
}
