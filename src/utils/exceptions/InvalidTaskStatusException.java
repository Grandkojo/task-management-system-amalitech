package utils.exceptions;

public class InvalidTaskStatusException extends Exception {
    public InvalidTaskStatusException(Object var4){
        super("Invalid task status type: '" + var4 + "'. Must be 'Pending', 'In Progess' or 'Completed'.\"");
    }
}
