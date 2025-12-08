package utils.exceptions;

public class TaskFullException extends Exception {
    public TaskFullException(String name){
        super("Task list is full, could not add task to project: " + name);
    }
}
