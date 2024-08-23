package java_projects.demo.exceptions;

public class FriendshipInvalidException extends Exception {
    /**
     * Return a validation error with chosen message
     *
     * @param message - the message we choose
     */
    public FriendshipInvalidException(String message) {
        super(message);
    }
}
