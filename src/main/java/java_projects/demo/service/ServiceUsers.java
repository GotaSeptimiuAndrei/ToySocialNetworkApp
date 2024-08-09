package java_projects.demo.service;

import java_projects.demo.domain.User;
import java_projects.demo.domain.UserProfile;
import java_projects.demo.exceptions.SecurityFaultException;
import java_projects.demo.repository.IRepository;
import java_projects.demo.repository.UsersRepository;
import java_projects.demo.validators.UserValidator;
import java_projects.demo.validators.SqlInjectionValidator;
import java_projects.demo.utils.PasswordEncryption;

import java.util.ArrayList;
import java.util.Collection;

public class ServiceUsers {

    private final IRepository<String, User> usersRepo;

    /**
     * Constructor for creating a service
     *
     * @param usersRepo - repository of users
     */
    public ServiceUsers(IRepository<String, User> usersRepo) {
        this.usersRepo = usersRepo;
    }

    /**
     * @param username - the username of the user we are looking for
     * @return a User object - the user with given username
     */
    public User getUserByUsername(String username) throws Exception {
        User user = usersRepo.findById(username);

        if (user == null)
            throw new Exception("User not found!");

        return user;
    }

    /**
     * @param username - the username of the user we are looking for
     * @return UserProfile
     */
    public UserProfile getUserProfileByUsername(String username) throws Exception {
        UserProfile userProfile = ((UsersRepository) this.usersRepo).getUserProfileById(username);
        if (userProfile == null)
            throw new Exception("User not found!");
        return userProfile;
    }

    /**
     * Add a new user in the list
     *
     * @param firstName of the user
     * @param lastName  of the user
     * @throws Exception if the parameters are not suitable for a user
     */
    public void addUser(String username, String password, String firstName, String lastName, String email,
                        String gender) throws Exception {
        UserValidator.validate(firstName, lastName, email, username, password, gender);

        String SALT = PasswordEncryption.getSalt(16);
        String SECRET_KEY = "my-very-strong-password";

        // Encrypt the password using the salt and secret key
        String securePassword = PasswordEncryption.encrypt(password, SALT, SECRET_KEY);

        User user = new User(username, securePassword, firstName, lastName, email, gender);
        usersRepo.add(user);
    }

    /**
     * Transform an Iterable object into a collection
     *
     * @param iter the iterable object we transform
     * @return the collection object we just created
     */
    public static <E> Collection<E> makeCollection(Iterable<E> iter) {
        Collection<E> list = new ArrayList<>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }

    /**
     * Return a list with all users from repository
     *
     * @return the list with the users
     */
    public ArrayList<User> getAllUsers() {
        Collection<User> iterable = makeCollection(usersRepo.findAll());
        return new ArrayList<>(iterable);
    }

    /**
     * Returns a list with all users that have given name in their firstname or in their lastname
     *
     * @param name - String - the name we search
     * @return - ArrayList <User> - all users that have name
     */
    public ArrayList<User> getUsersByPartialName(String name) {
        if (name.isEmpty())
            return new ArrayList<>();
        ArrayList<User> matchingUsers = new ArrayList<>();

        name = name.toLowerCase();
        Iterable<User> allUsers = this.getAllUsers();
        for (User user : allUsers) {
            if (user.getFirstName().toLowerCase().contains(name) || user.getLastName().toLowerCase().contains(name))
                matchingUsers.add(user);
        }

        return matchingUsers;
    }

    public void updateDescription(String newDescription, String username) throws SecurityFaultException {
        SqlInjectionValidator.validate(newDescription, username);
        ((UsersRepository) this.usersRepo).updateDescription(newDescription, username);
    }

    public void changeProfilePicturePath(String newPath, String username) throws SecurityFaultException {
        SqlInjectionValidator.validate(newPath, username);
        ((UsersRepository) this.usersRepo).changeProfilePicturePath(newPath, username);
    }
}
