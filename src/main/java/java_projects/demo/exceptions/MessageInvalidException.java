package java_projects.demo.exceptions;

public class MessageInvalidException extends Exception {
    /**
     * Return a validation error with chosen message
     *
     * @param message - the message we choose
     */
    public MessageInvalidException(String message) {
        super(message);
    }
}
