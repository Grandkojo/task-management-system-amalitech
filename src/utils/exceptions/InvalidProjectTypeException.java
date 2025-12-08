package utils.exceptions;

public class InvalidProjectTypeException extends Exception {
    public InvalidProjectTypeException(String type){
        super("Invalid project type: '" + type + "'. Must be 'Software' or 'Hardware'.");    }
}
