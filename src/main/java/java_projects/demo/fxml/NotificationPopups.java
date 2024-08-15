package java_projects.demo.fxml;

import javafx.scene.control.Alert;

public class NotificationPopups {

    /**
     * Make a popup windows that display message
     *
     * @param message - String
     */
    public static void errorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Oops, there was an error!");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
