package utils.exceptions;

public class ProjectNotFoundException extends Exception {
    public ProjectNotFoundException(){
        super("Project not found in list");
    }
}
