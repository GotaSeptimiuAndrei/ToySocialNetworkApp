package java_projects.demo.fxml.widgets_generators;

import java_projects.demo.fxml.NotificationPopups;
import java_projects.demo.fxml.scene_changer.HomepageSceneChanger;
import java_projects.demo.service.ServiceFriendships;
import java_projects.demo.service.ServiceMessages;
import java_projects.demo.service.ServiceUsers;
import java_projects.demo.service.Services;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class ButtonsGenerator {
    ServiceUsers serviceUsers;
    ServiceFriendships serviceFriendships;
    ServiceMessages serviceMessages;

    public ButtonsGenerator(Services services) {
        this.serviceUsers = services.getServiceUsers();
        this.serviceFriendships = services.getServiceFriendships();
        this.serviceMessages = services.getServiceMessages();
    }

    /**
     * A method that creates a button that adds the friendship between 2 users
     *
     * @param username1 - the username of the first friend
     * @param username2 - the username of the second friend
     * @return Button
     */
    protected Button makeButtonAddFriend(String username1, String username2) {
        Button button = new Button("Add friend");
        button.setId("main_button");
        button.setOnAction(event -> {
            try {
                serviceFriendships.addFriendship(username1, username2);
            } catch (Exception e) {
                NotificationPopups.errorPopup(e.getMessage());
            }
        });
        return button;
    }

    /**
     * A method that creates a button that accept the friend request between user1 and user2
     *
     * @param username1 - the username of the first friend
     * @param username2 - the username of the second friend
     * @return Button
     */
    protected Button makeButtonAcceptFriendRequest(String username1, String username2) {
        Button button = new Button("Accept");
        button.setId("main_button");
        button.setOnAction(event -> {
            try {
                serviceFriendships.acceptFriendRequest(username1, username2);
            } catch (Exception e) {
                NotificationPopups.errorPopup(e.getMessage());
            }
        });
        return button;
    }

    /**
     * A method that creates a button that decline the friend request between user1 and user2
     *
     * @param username1 - the username of the first friend
     * @param username2 - the username of the second friend
     * @return Button
     */
    protected Button makeButtonDeclineFriendRequest(String username1, String username2) {
        Button button = new Button("Decline");
        button.setId("secondary_button");
        button.setOnAction(event -> {
            try {
                serviceFriendships.declineFriendRequest(username1, username2);
            } catch (Exception e) {
                NotificationPopups.errorPopup(e.getMessage());
            }
        });
        return button;
    }

    /**
     * A method that creates a button that decline the friend request between user1 and user2
     *
     * @param username1 - the username of the first friend
     * @param username2 - the username of the second friend
     * @return Button
     */
    protected Button makeButtonCancelFriendRequest(String username1, String username2) {
        Button button = new Button("Cancel friend request");
        button.setId("secondary_button");
        button.setOnAction(event -> {
            try {
                serviceFriendships.removeFriendship(new ArrayList<>(Arrays.asList(username1, username2)));
            } catch (Exception e) {
                NotificationPopups.errorPopup(e.getMessage());
            }
        });
        return button;
    }

    /**
     * A method that creates a button that decline the friend request between user1 and user2
     *
     * @param username1 - the username of the first friend
     * @param username2 - the username of the second friend
     * @return Button
     */
    protected Button makeButtonDeleteFriend(String username1, String username2) {
        Button button = new Button("Delete friend");
        button.setId("secondary_button");
        button.setOnAction(event -> {
            try {
                serviceFriendships.removeFriendship(new ArrayList<>(Arrays.asList(username1, username2)));
            } catch (Exception e) {
                NotificationPopups.errorPopup(e.getMessage());
            }
        });
        return button;
    }

    public Button makeButtonFriendProfile(Stage stage, String loggedUser, String friend) {
        Button button = new Button("Profile");
        button.setId("main_button");
        button.setOnAction(event -> {
            try {
                Services services = new Services(serviceUsers, serviceFriendships, serviceMessages);
                HomepageSceneChanger.changeSceneToHomeProfile(stage, loggedUser, friend, services);
            } catch (Exception e) {
                NotificationPopups.errorPopup(e.getMessage());
            }
        });
        return button;
    }

    public Button makeButtonSendMessage(Stage stage, String loggedUser, String friend) {
        Button sendMessageButton = new Button("Messages");
        sendMessageButton.setId("main_button");
        sendMessageButton.setOnAction(event -> {
            try {
                Services services = new Services(serviceUsers, serviceFriendships, serviceMessages);
                HomepageSceneChanger.changeSceneToHomeInbox(stage, loggedUser, friend, services);
            } catch (Exception e) {
                NotificationPopups.errorPopup(e.getMessage());
            }
        });
        return sendMessageButton;
    }
}
