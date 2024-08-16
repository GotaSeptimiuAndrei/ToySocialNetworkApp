package java_projects.demo.fxml.homepage;

import java_projects.demo.domain.Friendship;
import java_projects.demo.domain.User;
import java_projects.demo.fxml.NotificationPopups;
import java_projects.demo.fxml.widgets_generators.VBoxesGenerator;
import java_projects.demo.utils.events.FriendshipChangeEvent;
import java_projects.demo.utils.events.UsersEvent;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class SearchController extends MainController {
    @FXML
    private TextField textFieldSearch;

    @FXML
    private ListView<VBoxesGenerator.FriendVBox> listViewMatchingUsers;
    private ObservableList<VBoxesGenerator.FriendVBox> modelUsers = FXCollections.observableArrayList();

    public void setUp() {
        this.serviceFriendships.addObserver(this, this.username);
        this.serviceMessages.addObserver(this, this.username);
    }

    /**
     * Updates all objects from page according to db changes
     */
    public void updateSearch() throws Exception {
        modelUsers.clear();
        ArrayList<User> matchingUsers = serviceUsers.getUsersByPartialName(textFieldSearch.getText());
        for (User user : matchingUsers) {
            if (Objects.equals(user.getUsername(), this.getUsername()))
                continue;

            Stage stage = (Stage) buttonSearch.getScene().getWindow();
            modelUsers.add(vBoxesGenerator.makeUserVBox(stage, this.username, user.getUsername()));
        }
    }

    /**
     * Initialize the page
     */
    public void initialize() {
        listViewMatchingUsers.setItems(modelUsers);
        textFieldSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    updateSearch();
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificationPopups.errorPopup(e.getMessage());
                }
            }
        });
        listViewMatchingUsers.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        listViewMatchingUsers.getSelectionModel().select(-1);
                    }
                });
            }
        });
    }

    @Override
    public void updateFriendships(UsersEvent<String> event) {
        FriendshipChangeEvent friendshipChangeEvent = (FriendshipChangeEvent) event;
        Friendship friendship = friendshipChangeEvent.getNewFriendship();
        String username2 = friendshipChangeEvent.getOtherFriendInvolved(this.username);
        for (var userBox : modelUsers) {
            if (!userBox.getUsername2().equals(username2)
                    && !userBox.getUsername2().equals(this.username)) continue;
            userBox.updateFriendStateButtons(friendship);
        }
    }
}
