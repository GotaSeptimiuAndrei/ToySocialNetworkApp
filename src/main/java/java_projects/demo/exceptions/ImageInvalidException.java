package java_projects.demo.exceptions;

public class ImageInvalidException extends Exception {
    /**
     * Return a validation error with default message
     */
    public ImageInvalidException() {
        super("Invalid data for friendship!");
    }

    /**
     * Return a validation error with chosen message
     *
     * @param message - the message we choose
     */
    public ImageInvalidException(String message) {
        super(message);
    }
}
