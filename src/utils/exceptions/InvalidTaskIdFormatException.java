package utils.exceptions;

public class InvalidTaskIdFormatException extends Exception {
    public InvalidTaskIdFormatException(){
        super("Invalid Task ID format. Use pattern TSK### (eg., TSK001)");
    }
}
