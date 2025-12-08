package utils.exceptions;

public class ProjectFullException extends Exception {
    public ProjectFullException(String name){
        super("Project list is full, could not add project: " + name);
    }
}
