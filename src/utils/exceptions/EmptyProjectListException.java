package utils.exceptions;

public class EmptyProjectListException extends RuntimeException {
    public EmptyProjectListException(String message){
        super(message);
    }
}
