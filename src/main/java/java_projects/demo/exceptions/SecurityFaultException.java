package java_projects.demo.exceptions;

public class SecurityFaultException extends Exception {
    /**
     * Return a validation error with default message
     */
    public SecurityFaultException() {
        super("Security fault! Action not allowed! ");
    }
}
