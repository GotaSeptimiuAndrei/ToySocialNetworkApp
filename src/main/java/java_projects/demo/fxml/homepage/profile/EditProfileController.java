package java_projects.demo.fxml.homepage.profile;

import java_projects.demo.domain.UserProfile;
import java_projects.demo.exceptions.SecurityFaultException;
import java_projects.demo.fxml.NotificationPopups;
import java_projects.demo.fxml.homepage.MainController;
import java_projects.demo.validators.ImageValidator;
import java_projects.demo.utils.FileChooser;
import javafx.fxml.FXML;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class EditProfileController extends MainController {
    @FXML
    private Label firstnameLabel;
    @FXML
    private Label lastnameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private Button changeDescriptionButton;
    @FXML
    private Button cancelChangeDescriptionButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Button changePictureButton;

    private UserProfile userProfile;

    /**
     * A method that loads the current user profile picture
     *
     * @param user - UserProfile
     */
    public void loadProfilePicture(UserProfile user) {
        if (user.getProfilePicturePath() == null) {
            this.imageView.setImage(new Image("pictures/defaultProfilePicture.jpg"));
        } else {
            try {
                InputStream stream = new FileInputStream(user.getProfilePicturePath());
                Image image = new Image(stream);
                imageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                NotificationPopups.errorPopup("Your image is being changed. The changes will appear in short time");
                this.imageView.setImage(new Image("pictures/defaultProfilePicture.jpg"));
            }
        }
    }

    /**
     * A method that makes default configurations for current controller
     */
    public void setUp() throws Exception {
        this.userProfile = serviceUsers.getUserProfileByUsername(this.username);
        this.firstnameLabel.setText(this.userProfile.getFirstName());
        this.lastnameLabel.setText(this.userProfile.getLastName());
        this.usernameLabel.setText("@" + this.userProfile.getUsername());
        this.descriptionTextField.setText(this.userProfile.getDescription());
        loadProfilePicture(userProfile);

        this.serviceMessages.addObserver(this, this.username);
        this.serviceFriendships.addObserver(this, this.username);
    }

    /**
     * Method that returns the path where will be saved the new picture
     *
     * @return - String
     */
    private String computeNewPicturePath() {
        if (this.userProfile.getProfilePicturePath() == null) {
            File f = new File("src//main//resources//pictures//users_pictures//" + this.username);
            f.mkdir();
        }
        return "src//main//resources//pictures//users_pictures//" + this.username + "//profilePicture.png";
    }

    /**
     * Copy the image from source path to destination path
     *
     * @param sourcePath      - String
     * @param destinationPath - String
     */
    private void imageCopy(String sourcePath, String destinationPath) {
        File file = new File(destinationPath);
        Image imageToBeSaved = new Image(sourcePath);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(imageToBeSaved, null), "png", file);
            imageView.setImage(imageToBeSaved);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that open file chooser and change profile picture to the selected one
     *
     * @throws Exception if the selected photo is not suitable
     */
    @FXML
    private void changePictureClick() throws Exception {
        FileChooser fileChooserSample = new FileChooser();
        File file = fileChooserSample.getFilePath(new Stage());
        if (file == null)
            return;
        ImageValidator.validate(file.getPath());
        String newPath = computeNewPicturePath();
        imageCopy(file.getAbsolutePath(), newPath);
        this.serviceUsers.changeProfilePicturePath(newPath, username);
    }

    /**
     * A method that updates the description in database and on profile to the descriptionTextField input
     */
    @FXML
    private void changeDescriptionClick() throws SecurityFaultException {
        String newDescription = descriptionTextField.getText();
        serviceUsers.updateDescription(newDescription, this.username);
        this.userProfile.setDescription(newDescription);
    }

    /**
     * Reset descriptionTextField box
     */
    @FXML
    private void cancelChangeDescriptionClick() {
        descriptionTextField.setText(this.userProfile.getDescription());
    }
}
