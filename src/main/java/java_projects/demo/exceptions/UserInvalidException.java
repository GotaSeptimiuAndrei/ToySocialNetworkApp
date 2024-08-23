package java_projects.demo.exceptions;

public class UserInvalidException extends Exception {
    /**
     * Return a validation error with chosen message
     *
     * @param message - the message we choose
     */
    public UserInvalidException(String message) {
        super(message);
    }
}
