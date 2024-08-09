package java_projects.demo.validators;

import java_projects.demo.exceptions.FriendshipInvalidException;
import java_projects.demo.exceptions.SecurityFaultException;

public class FriendshipValidator {
    /**
     * Validate if a friendship content is valid
     *
     * @throws FriendshipInvalidException if the friendship is not valid
     */
    public static void validate(String usernameFriend1, String usernameFriend2) throws FriendshipInvalidException, SecurityFaultException {
        String message = "";
        if (usernameFriend1.isEmpty())
            message = message + "Invalid first name! ";
        if (usernameFriend2.isEmpty())
            message = message + "Invalid last name! ";
        if (usernameFriend1.equals(usernameFriend2))
            message = message + "Invalid pair of ids! ";
        if (usernameFriend1.equals(usernameFriend2))
            message = message + "Users must be different! ";

        SqlInjectionValidator.validate(usernameFriend1, usernameFriend2);

        if (!message.isEmpty())
            throw new FriendshipInvalidException(message);
    }
}
