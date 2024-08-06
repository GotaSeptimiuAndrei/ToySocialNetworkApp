package java_projects.demo.domain;

import java.util.Objects;

public class User extends Entity<String>{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;

    /**
     * Constructor for creating a user with chosen parameters
     * @param firstName - String - firstname of the user
     * @param lastName - String - the lastname of the user
     * @param email - String - the email of the user
     * @param username - String  - the username of the user
     * @param password - String - user password
     * @param gender - String - user gender
     */
    public User(String username, String password, String firstName, String lastName, String email, String gender){
        super(username);
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }

    /**
     * Returns the firstName of the current user
     * @return a string with the firstname
     */
    public String getFirstName(){
        return this.firstName;
    }

    /**
     * Returns the last name of the current user
     * @return a string with the lastname
     */
    public String getLastName(){
        return this.lastName;
    }

    /**
     * Updates the firstname of the user
     * @param newFirstName the new firstName
     */
    public void setFirstName(String newFirstName){
        this.firstName = newFirstName;
    }

    /**
     * Updates the lastname if the user
     * @param newLastName the new lastName
     */
    public void setLastName(String newLastName){
        this.lastName = newLastName;
    }

    /**
     * Returns a string with {id, firstname, lastname} - printable form of user
     * @return string
     */
    @Override
    public String toString(){
        return "User{" +
                "ID='" + getId() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName +
                '}';
    }

    /**
     * Returns the hashCode of the user
     * @return int
     */
    @Override
    public int hashCode(){
        return Objects.hash(getFirstName() ,getLastName());
    }


    /**
     * Check if two users have same values of parameters
     * @param other - User - the user we compare with current user
     * @return - boolean - true or false
     */
    public boolean equals(User other){
        if(other == null) return false;

        return Objects.equals(other.getId(), this.getId()) &&
                Objects.equals(other.getFirstName(), this.getFirstName()) &&
                Objects.equals(other.getLastName(), this.getLastName()) &&
                Objects.equals(other.getUsername(), this.getUsername()) &&
                Objects.equals(other.getPassword(), this.getPassword()) &&
                Objects.equals(other.getGender(), this.getGender()) &&
                Objects.equals(other.getEmail(), this.getEmail());
    }

    /**
     * Returns the email of a user
     * @return String - the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changes the email of a user
     * @param email - String - the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the username of a user
     * @return String - the username
     */
    public String getUsername() {
        return getId();
    }

    /**
     * Changes the username of a user
     * @param username - String - new username
     */
    public void setUsername(String username) {
        this.setId(username);
    }

    /**
     * Returns the password of the user
     * @return String - password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Changes the password of the user
     * @param password - String - new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the gender of the user
     * @return String - the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user
     * @param gender - new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
}

