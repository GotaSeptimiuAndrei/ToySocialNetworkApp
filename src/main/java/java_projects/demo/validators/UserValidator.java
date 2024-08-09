package java_projects.demo.validators;

import java_projects.demo.exceptions.SecurityFaultException;
import java_projects.demo.exceptions.UserInvalidException;

public class UserValidator {

    /**
     * Validate if the user potential content is valid.
     * Every string should be nonempty
     *
     * @param firstName - String - the firstname we validate
     * @param lastName  - String - the lastname we validate
     * @param email     - String - the email we check to be valid
     * @param username  - String - the username we check to be valid
     * @param password  - String - the password we check to be valid
     * @param gender    - String - the gender we check to be valid
     */
    public static void validate(String firstName, String lastName, String email,
                                String username, String password, String gender) throws UserInvalidException, SecurityFaultException, SecurityFaultException {
        String message = "";
        if (firstName.isEmpty())
            message = message + "Invalid first name! ";
        if (lastName.isEmpty())
            message = message + "Invalid last name! ";
        if (email.isEmpty())
            message = message + "Invalid email! ";
        if (username.isEmpty())
            message = message + "Invalid username! ";
        if (password.isEmpty())
            message = message + "Invalid password! ";
        if (gender == null)
            message = message + "You have to choose an option for gender! ";
        else if (gender.isEmpty())
            message = message + "Invalid gender! ";

        if (!message.isEmpty())
            throw new UserInvalidException(message);

        SqlInjectionValidator.validate(firstName, lastName, email, username, password, gender);

    }
}
