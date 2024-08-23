package java_projects.demo.fxml.homepage;

import java_projects.demo.fxml.NotificationPopups;
import java_projects.demo.fxml.widgets_generators.VBoxesGenerator;
import java_projects.demo.utils.events.EventType;
import java_projects.demo.utils.events.FriendshipChangeEvent;
import java_projects.demo.utils.events.UsersEvent;
import java_projects.demo.domain.Friendship;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class NotificationsController extends MainController {
    @FXML
    protected ListView<VBox> listViewNotifications;

    private ObservableList<VBox> usersObservableList;

    private boolean noNotification;

    public void setUp() {
        try {
            loadNotifications();
            listViewNotifications.setItems(usersObservableList);
            this.serviceFriendships.addObserver(this, this.username);
            this.serviceMessages.addObserver(this, this.username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            NotificationPopups.errorPopup("Notifications can not be opened!");
        }
        this.serviceFriendships.addObserver(this, this.username);
    }

    /**
     * Method that loads noNotificationPicture on notifications listView
     */
    private void noNotificationMessage() {
        try {
            String absolutePath = new File("src/main/resources/java_projects/demo/pictures/noNotifications.png").getAbsolutePath();
            Image image = new Image("file:" + absolutePath);
            VBox vBox = new VBox(new ImageView(image));
            usersObservableList.add(vBox);
            noNotification = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            NotificationPopups.errorPopup("Error uploading notification image");
        }
    }

    /**
     * Method that loads all notifications of the current user
     */
    private void loadNotifications() {
        ArrayList<VBox> notificationsVBoxes = new ArrayList<>();
        Stage currentStage = (Stage) buttonNotification.getScene().getWindow();
        serviceFriendships.getAllFriendRequestsReceived(username).forEach(friendRequest -> {
            try {
                notificationsVBoxes.add(vBoxesGenerator.makePendingFriendRequestBox(currentStage, username, friendRequest.getOtherFriend(username)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                NotificationPopups.errorPopup("Notifications loading failed!");
            }
        });

        usersObservableList = FXCollections.observableList(notificationsVBoxes);
        if (notificationsVBoxes.isEmpty())
            noNotificationMessage();
    }

    /**
     * Method that removes accepted or declined friend requests
     *
     * @param usernameFriend - String
     */
    private void removeVBoxPendingFriendRequest(String usernameFriend) {
        VBox vBoxToRemove = null;
        for (VBox vbox : usersObservableList) {
            if (Objects.equals(((VBoxesGenerator.FriendVBox) vbox).getUsername2(), usernameFriend))
                vBoxToRemove = vbox;
        }
        usersObservableList.remove(vBoxToRemove);
    }

    /**
     * Method that removes accepted or declined friend requests when FriendshipChangeEvent occurred
     */
    private void removeFriendRequest(FriendshipChangeEvent event) {
        Friendship friendRequest = event.getOldFriendship();
        if (Objects.equals(friendRequest.getIdFriend1(), username))
            removeVBoxPendingFriendRequest(friendRequest.getIdFriend2());
        else
            removeVBoxPendingFriendRequest(friendRequest.getIdFriend1());
    }

    /**
     * Method that add new friendRequest when is received
     *
     * @param event - FriendshipChangeEvent
     */
    private void addFriendRequest(FriendshipChangeEvent event) throws Exception {
        Stage currentStage = (Stage) buttonNotification.getScene().getWindow();
        Friendship friendship = event.getNewFriendship();
        usersObservableList.add(vBoxesGenerator.makePendingFriendRequestBox(currentStage, username, friendship.getOtherFriend(username)));
    }

    /**
     * Method that updates friendRequests when there is a FriendshipChangeEvent
     */
    @Override
    public void updateFriendships(UsersEvent<String> event) {
        if (noNotification) {
            noNotification = false;
            usersObservableList.clear();
        }
        try {
            if (event.getTypeOfEvent() == EventType.ADD)
                addFriendRequest((FriendshipChangeEvent) event);
            else
                removeFriendRequest((FriendshipChangeEvent) event);
        } catch (Exception e) {
            e.printStackTrace();
            NotificationPopups.errorPopup("Error");
        }
        if (usersObservableList.isEmpty())
            noNotificationMessage();
    }

    public void initialize() {
        listViewNotifications.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> Platform.runLater(new Runnable() {
            public void run() {
                listViewNotifications.getSelectionModel().select(-1);
            }
        }));
    }
}
