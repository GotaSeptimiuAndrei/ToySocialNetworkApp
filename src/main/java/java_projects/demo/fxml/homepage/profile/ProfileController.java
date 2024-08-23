package java_projects.demo.fxml.homepage.profile;

import java_projects.demo.domain.UserProfile;
import java_projects.demo.exceptions.SecurityFaultException;
import java_projects.demo.fxml.NotificationPopups;
import java_projects.demo.fxml.homepage.MainController;
import java_projects.demo.fxml.widgets_generators.VBoxesGenerator;
import java_projects.demo.fxml.scene_changer.HomepageSceneChanger;
import java_projects.demo.service.Services;
import java_projects.demo.utils.events.FriendshipChangeEvent;
import java_projects.demo.utils.events.UsersEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;

public class ProfileController extends MainController {
    @FXML
    private Label firstnameLabel;
    @FXML
    private Label lastnameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label description;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView editProfileButtonImageView;
    @FXML
    private VBox friendVBox;

    private VBoxesGenerator.FriendVBox friendshipStateButtonsVBox;

    /**
     * A method that loads friend profile components
     *
     * @param profileOwner - String
     */
    public void setUpFriendProfile(String profileOwner) throws Exception {
        Stage stage = (Stage) friendVBox.getScene().getWindow();
        friendshipStateButtonsVBox = vBoxesGenerator.makeFriendshipChangeVBox(stage, this.username, profileOwner);
        friendVBox.getChildren().add(friendshipStateButtonsVBox);
    }

    /**
     * Method that loads our own profile
     */
    public void setUpOwnProfile() {
        editProfileButtonImageView.setOnMouseClicked(e -> {
            clickEditProfile();
        });
    }

    /**
     * A method that loads the current user profile picture
     *
     * @param user - UserProfile
     */
    public void loadProfilePicture(UserProfile user) throws SecurityFaultException {
        // Path to the default profile picture
        String defaultPicturePath = "src/main/resources/java_projects/demo/pictures/defaultProfilePicture.jpg";

        if (user.getProfilePicturePath() == null) {
            try {
                // If the user's profile picture is null, set the default picture
                System.out.println("Profile picture path is null.");
                user.setProfilePicturePath(defaultPicturePath);
                serviceUsers.changeProfilePicturePath(defaultPicturePath, user.getUsername());
                String absolutePath = new File(defaultPicturePath).getAbsolutePath();
                Image image = new Image("file:" + absolutePath);
                this.imageView.setImage(image);
            } catch (SecurityFaultException e) {
                e.printStackTrace();
                NotificationPopups.errorPopup("Unable to update profile picture path in the database.");
            }
        } else {
            try {
                // Load the user's profile picture if it exists
                InputStream stream = new FileInputStream(user.getProfilePicturePath());
                Image image = new Image(stream);
                imageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                NotificationPopups.errorPopup("Unable to load profile picture. Using default picture.");

                // If loading the user's profile picture fails, set the default picture
                String absolutePath = new File(defaultPicturePath).getAbsolutePath();
                Image image = new Image("file:" + absolutePath);
                this.imageView.setImage(image);
            }
        }
    }


    /**
     * A method that makes configurations for current profile
     *
     * @param profileOwnerUsername - String
     */
    public void setUp(String profileOwnerUsername) throws Exception {
        UserProfile user = serviceUsers.getUserProfileByUsername(profileOwnerUsername);
        this.firstnameLabel.setText(user.getFirstName());
        this.lastnameLabel.setText(user.getLastName());
        this.usernameLabel.setText("@" + user.getUsername());
        this.description.setText(user.getDescription());
        loadProfilePicture(user);
        if (Objects.equals(profileOwnerUsername, this.username))
            setUpOwnProfile();
        else
            setUpFriendProfile(profileOwnerUsername);
        this.serviceFriendships.addObserver(this, this.username);
        this.serviceMessages.addObserver(this, this.username);
    }

    /**
     * A method that will open editProfile page for logged user
     */
    @FXML
    void clickEditProfile() {
        try {
            Stage stage = (Stage) buttonSearch.getScene().getWindow();
            Services services = new Services(serviceUsers, serviceFriendships, serviceMessages);
            HomepageSceneChanger.changeSceneToEditProfile(stage, username, services);
        } catch (Exception e) {
            NotificationPopups.errorPopup(e.getMessage());
        }
    }

    @Override
    public void updateFriendships(UsersEvent<String> event) {
        if (friendshipStateButtonsVBox == null)
            return;
        friendshipStateButtonsVBox.updateFriendStateButtons(((FriendshipChangeEvent) event).getNewFriendship());
    }
}
