package java_projects.demo.domain;

public class UserProfile extends User {
    private String description;
    private String profilePicturePath;

    /**
     * Constructor for creating a user with chosen parameters
     *
     * @param username  - String  - the username of the user
     * @param password  - String - user password
     * @param firstName - String - firstname of the user
     * @param lastName  - String - the lastname of the user
     * @param email     - String - the email of the user
     * @param gender    - String - user gender
     * @param description - String - the self-description of the user
     * @param profilePicturePath - String - the path to the profile picture
     */
    public UserProfile(String username, String password, String firstName, String lastName, String email, String gender,
                       String description, String profilePicturePath) {
        super(username, password, firstName, lastName, email, gender);
        this.description = description;
        this.profilePicturePath = profilePicturePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
}

