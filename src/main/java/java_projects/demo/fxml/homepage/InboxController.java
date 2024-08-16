package java_projects.demo.fxml.homepage;

import java_projects.demo.config.Config;
import java_projects.demo.domain.Message;
import java_projects.demo.domain.User;
import java_projects.demo.domain.UserProfile;
import java_projects.demo.fxml.NotificationPopups;
import java_projects.demo.fxml.widgets_generators.VBoxesGenerator;
import java_projects.demo.service.ServiceUsers;
import java_projects.demo.utils.DataTypeConvertors;
import java_projects.demo.utils.events.EventType;
import java_projects.demo.utils.events.MessageEvent;
import java_projects.demo.utils.events.UsersEvent;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class InboxController extends MainController {
    ObservableList<HBox> modelUsers = FXCollections.observableArrayList();
    ObservableList<HBox> modelMessages = FXCollections.observableArrayList();

    private String conversationFriendUsername;

    @FXML
    private ListView<HBox> usersListView;
    @FXML
    private ListView<HBox> messagesListView;

    @FXML
    private TextField sendMessageTextField;
    @FXML
    private Button sendMessageButton;
    @FXML
    private Label friendNameLabel;
    @FXML
    private ImageView friendImageView;

    private void sortConversationsDescending() {
        modelUsers.sort((box1, box2) -> {
            if (((ConversationBox) box1).getTimeOfLastMessage().isBefore(((ConversationBox) box2).getTimeOfLastMessage()))
                return 1;
            if (((ConversationBox) box1).getTimeOfLastMessage().isAfter(((ConversationBox) box2).getTimeOfLastMessage()))
                return -1;
            return 0;
        });
    }

    public void updateConversations(Message message) {
        boolean conversationFound = false;
        for (var conversation : modelUsers) {
            if (Objects.equals(((ConversationBox) conversation).getFriendUsername(),
                    message.getTheUsernameOfTheOtherUser(this.username))) {
                ((ConversationBox) conversation).updateLastMessage(message);
                conversationFound = true;
            }
        }
        if (!conversationFound)
            modelUsers.add(new ConversationBox(message, username, serviceUsers));
        sortConversationsDescending();
        if (!message.isReceived() && message.getReceiver().equals(this.username))
            serviceMessages.setMessagesReceived(message);
    }

    public void setMessageSent(Message message) {
        for (var currentMessage : modelMessages) {
            if (((MessageBox) currentMessage).getMessageId() == null)
                if (message.getMessageContent().equals(((MessageBox) currentMessage).getMessageContent())) {
                    ((MessageBox) currentMessage).setMessageSent(message);
                }
        }
    }

    public void changeMessagesState(Message message) {
        for (var currentMessage : modelMessages) {
            if (!Objects.equals(message.getId(), ((MessageBox) currentMessage).getMessageId()))
                continue;
            if (!(Objects.equals(message.getSender(), this.username)))
                continue;
            if (message.isSeen()) {
                ((MessageBox) currentMessage).setMessageSeen(message);
                continue;
            }
            if (message.isReceived())
                ((MessageBox) currentMessage).setMessageReceived(message);
        }
    }

    public void addNewReceivedMessage(Message message) {
        if (!message.getTheUsernameOfTheOtherUser(this.username).equals(conversationFriendUsername))
            return;
        modelMessages.add(new MessageBox(message, this.username));
        serviceMessages.setMessageSeen(message);
    }

    @Override
    public void updateMessages(UsersEvent<String> event) {
        MessageEvent messageEvent = (MessageEvent) event;
        if (event.getTypeOfEvent() == EventType.ADD) {
            Message message = messageEvent.getNewMessage();
            if (Objects.equals(message.getSender(), this.username))
                setMessageSent(message);
            else if (Objects.equals(message.getReceiver(), this.username))
                addNewReceivedMessage(message);
            updateConversations(message);
            return;
        }
        if (event.getTypeOfEvent() == EventType.UPDATE) {
            Message message = messageEvent.getNewMessage();
            if (Objects.equals(message.getSender(), this.username)) {
                changeMessagesState(message);
            }
        }
    }

    static abstract class AbstractConversationBox extends HBox {
        protected VBox nameVBox;

        public abstract String getFriendUsername();
    }

    static class ConversationBox extends AbstractConversationBox {
        private VBox timeVBox;
        private String friendUsername;
        private Message lastMessage;

        public ConversationBox(Message message, String username, ServiceUsers serviceUsers) {
            this.lastMessage = message;
            this.friendUsername = message.getTheUsernameOfTheOtherUser(username);
            try {
                nameVBox = VBoxesGenerator.createUserNameAndUsernameVBox(serviceUsers.getUserByUsername(friendUsername));
                nameVBox.setMinWidth(115);
                nameVBox.setMaxWidth(115);
            } catch (Exception e) {
                NotificationPopups.errorPopup("Conversations loading failure: critical! ");
                e.printStackTrace();
            }
            timeVBox = VBoxesGenerator.createMessageTimeVBox(message.getMessageTime());
            timeVBox.setMinWidth(65);
            timeVBox.setMaxWidth(65);
            this.getChildren().add(nameVBox);
            this.getChildren().add(timeVBox);
        }

        public String getFriendUsername() {
            return friendUsername;
        }

        public LocalDateTime getTimeOfLastMessage() {
            return this.lastMessage.getMessageTime();
        }

        public void updateLastMessageTime(LocalDateTime dateTime) {
            this.lastMessage.setMessageTime(dateTime);
            timeVBox = VBoxesGenerator.createMessageTimeVBox(dateTime);
            timeVBox.setMinWidth(65);
            timeVBox.setMaxWidth(65);
            this.getChildren().setAll(nameVBox, timeVBox);
        }

        public void updateLastMessage(Message message) {
            updateLastMessageTime(message.getMessageTime());
            this.lastMessage = message;
        }

        public Message getLastMessage() {
            return this.lastMessage;
        }

    }

    static class MessageBox extends HBox {
        @FXML
        private BorderPane outerBox = new BorderPane();
        @FXML
        private VBox messageContentVBox = new VBox();
        @FXML
        private Label messageLabel = new Label();
        @FXML
        private Label timeLabel = new Label();
        @FXML
        private Label statusLabel = new Label();
        @FXML
        private HBox messageInfoHBox = new HBox();

        private Message message = null;

        private String messageContent = null;

        public void setUp(String messageContent) {
            messageLabel.setMaxWidth(300);
            messageLabel.setWrapText(true);
            messageLabel.setTextAlignment(TextAlignment.JUSTIFY);
            messageLabel.setText(messageContent);

            if (this.message == null)
                timeLabel.setText(DataTypeConvertors.getCompressedTimeStringFromDateTime(LocalDateTime.now()));
            else
                timeLabel.setText(DataTypeConvertors.getCompressedTimeStringFromDateTime(message.getMessageTime()));
            timeLabel.setOpacity(0.5);
            statusLabel.setText(" ");
            statusLabel.setOpacity(0.5);

            messageInfoHBox.setAlignment(Pos.BOTTOM_RIGHT);
            messageInfoHBox.getChildren().setAll(timeLabel, statusLabel);
            messageInfoHBox.setMinWidth(60);

            messageContentVBox.getChildren().setAll(messageLabel, messageInfoHBox);
            outerBox.setCenter(messageContentVBox);
            outerBox.setStyle("-fx-alignment: CENTER_LEFT; -fx-column-halignment: CENTER");
            this.getChildren().setAll(outerBox);


        }

        public MessageBox(String messageContent) {
            this.messageContent = messageContent;
            this.setUp(messageContent);
            this.setAlignment(Pos.BASELINE_RIGHT);
            messageContentVBox.setStyle("-fx-background-color: " +
                    Config.getProperties().getProperty("sendingMessageColor"));
        }

        public MessageBox(Message message, String userUsername) {
            this.message = message;
            this.setUp(message.getMessageContent());
            if (userUsername.equals(message.getSender())) {
                this.setAlignment(Pos.BASELINE_RIGHT);
                messageContentVBox.setStyle("-fx-background-color: " +
                        Config.getProperties().getProperty("sendingMessageColor"));
                if (message.isSeen()) {
                    statusLabel.setText(" ৹৹ ");
                    statusLabel.setStyle("-fx-text-fill: " +
                            Config.getProperties().getProperty("seenMessage"));
                    statusLabel.setOpacity(0.7);
                }
                if (message.isReceived())
                    statusLabel.setText(" ৹৹ ");
                else
                    statusLabel.setText(" ৹ ");
            } else {
                messageContentVBox.setStyle("-fx-background-color: " +
                        Config.getProperties().getProperty("incomingMessageColor"));
                statusLabel.setText("");
            }

        }

        public Long getMessageId() {
            if (this.message == null) return null;
            return message.getId();
        }

        public String getReceiver() {
            if (this.message == null)
                return null;
            return this.message.getReceiver();
        }

        public LocalDateTime getMessageTime() {
            if (this.message == null)
                return null;
            return this.message.getMessageTime();
        }

        public String getMessageContent() {
            return this.messageContent;
        }

        public void setMessageSent(Message message) {
            statusLabel.setText(" ৹ ");
            this.message = message;
            this.messageContent = null;
        }

        public void setMessageReceived(Message message) {
            statusLabel.setText(" ৹৹ ");
            this.message = message;
        }

        public void setMessageSeen(Message message) {
            statusLabel.setText(" ৹৹ ");
            statusLabel.setStyle("-fx-text-fill: " +
                    Config.getProperties().getProperty("seenMessage"));
            this.message = message;
        }

    }

    private void loadLastConversations() {
        Iterable<Message> conversations = this.serviceMessages.getLastMessageFromEveryConversation(this.username);
        conversations.forEach(
                message -> {
                    modelUsers.add(new ConversationBox(message, username, serviceUsers));
                    if (!message.isReceived() && message.getReceiver().equals(this.username))
                        serviceMessages.setMessagesReceived(message);
                }
        );
        addOpenMessagesOnBoxClick(modelUsers);
        setButtonMessagesState(false);
    }

    /**
     * Sets the fields of friend data
     *
     * @param friendUsername
     */
    private void setFriendNameOnLabel(String friendUsername) {
        User user = null;
        try {
            user = serviceUsers.getUserByUsername(friendUsername);
        } catch (Exception e) {
            NotificationPopups.errorPopup("Error loading the user! ");
        }
        this.friendNameLabel.setText(user.getFirstName() + " " + user.getLastName() + "\n");
    }

    /**
     * Load the profile picture of selected user
     *
     * @param user - UserProfile
     */
    public void loadProfilePicture(UserProfile user) {
        if (user.getProfilePicturePath() == null) {
            this.friendImageView.setImage(new Image("pictures/defaultProfilePicture.jpg"));
        } else {
            try {
                InputStream stream = new FileInputStream(user.getProfilePicturePath());
                Image image = new Image(stream);
                friendImageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                NotificationPopups.errorPopup("Your image is being changed. The changes will appear in short time");
                this.friendImageView.setImage(new Image("pictures/defaultProfilePicture.jpg"));
            }
        }
    }

    /**
     * Method that will make the settings needed before opening a conversation
     *
     * @param friendUsername - String
     */
    private void setUpConversationWithFriend(String friendUsername) {
        sendMessageTextField.setOpacity(1);
        sendMessageButton.setOpacity(1);
        this.conversationFriendUsername = friendUsername;
        setFriendNameOnLabel(friendUsername);
        try {
            loadProfilePicture(this.serviceUsers.getUserProfileByUsername(friendUsername));
        } catch (Exception e) {
            NotificationPopups.errorPopup("Friend photo loading failure");
        }
    }

    /**
     * Loads all messages with friendUsername
     *
     * @param friendUsername
     */
    private void loadMessages(String friendUsername) {
        if (friendUsername == null) return;
        setUpConversationWithFriend(friendUsername);

        modelMessages.clear();
        this.serviceMessages.getAllMessagesBetween2Users(friendUsername, this.username).forEach(
                message -> {
                    modelMessages.add(new MessageBox(message, this.username));
                    if (Objects.equals(message.getReceiver(), this.username) && !message.isSeen())
                        this.serviceMessages.setMessageSeen(message);
                }
        );
        messagesListView.scrollTo(modelMessages.get(modelMessages.size() - 1));
    }

    /**
     * A method that sends the message from sendMessageTextField. The message must be not empty
     *
     * @throws Exception - if the message content is not accepted
     */
    @FXML
    private void sendMessage() throws Exception {
        String messageContent = sendMessageTextField.getText();
        if (messageContent.isEmpty()) return;
        sendMessageTextField.setText("");
        modelMessages.add(new MessageBox(messageContent));
        serviceMessages.addNewMessage(username, this.conversationFriendUsername, messageContent);
    }

    public void setUp(String friend) {
        try {
            sendMessageTextField.setOpacity(0);
            sendMessageButton.setOpacity(0);
            usersListView.setItems(modelUsers);
            messagesListView.setItems(modelMessages);
            this.conversationFriendUsername = friend;
            loadLastConversations();
            loadMessages(friend);
            this.serviceFriendships.addObserver(this, this.username);
            this.serviceMessages.addObserver(this, this.username);
        } catch (Exception e) {
            NotificationPopups.errorPopup(e.getMessage());
        }
    }

    @FXML
    private TextField searchFriendsTextField;

    public void initialize() {
        searchFriendsTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadSearchUsers(newValue);
            }
        });
        messagesListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        messagesListView.getSelectionModel().select(-1);
                    }
                });
            }
        });
        usersListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        usersListView.getSelectionModel().select(-1);
                    }
                });
            }
        });
    }

    ObservableList<HBox> modelSearch = FXCollections.observableArrayList();

    /**
     * Returns a set with all users where: 1. There is a conversation with loggedUser; 2.searchValue is matching with
     * their username
     *
     * @param searchValue - String
     * @return - Set<String>
     */
    public Set<String> getMatchingUsernamesFromConversations(String searchValue) {
        Set<String> usernames = new HashSet<>();
        modelUsers.forEach(userBox -> {
            String username = ((ConversationBox) userBox).getFriendUsername();
            if (username.contains(searchValue))
                usernames.add(username);
        });
        return usernames;
    }

    /**
     * Add current user in model search
     *
     * @param username - String
     */
    public void addUserInModelSearch(String username) {
        try {
            User friend = this.serviceUsers.getUserByUsername(username);
            modelSearch.add(new UserHBox(friend));
        } catch (Exception e) {
            NotificationPopups.errorPopup("Users loading failure");
        }
    }

    /**
     * Method that adds conversationUsers and friendUsers in modelSearch
     *
     * @param conversationsUsers -Set < String > conversationsUsers
     * @param friendsUsers       - Iterable < String > friendsUsers
     */
    public void populateModelSearch(Set<String> conversationsUsers, Iterable<String> friendsUsers) {
        modelSearch.clear();
        conversationsUsers.forEach(this::addUserInModelSearch);
        friendsUsers.forEach(friendUsername -> {
            if (!conversationsUsers.contains(friendUsername))
                addUserInModelSearch(friendUsername);
        });
        addOpenMessagesOnBoxClick(modelSearch);
    }

    /**
     * Method that enables open conversation on click for every hBox
     *
     * @param model - ObservableList < HBox
     */
    public void addOpenMessagesOnBoxClick(ObservableList<HBox> model) {
        model.forEach(box -> {
            AbstractConversationBox conversationBox = (AbstractConversationBox) box;
            box.setOnMouseClicked(e -> {
                loadMessages(conversationBox.getFriendUsername());
            });
        });

    }

    /**
     * Method that loads all users whose usernames are matching searchValue. First will load the users with existing
     * conversation and after that friends and user with pending friendRequests.
     *
     * @param searchValue - String
     */
    public void loadSearchUsers(String searchValue) {
        if (searchValue.isEmpty()) {
            usersListView.setItems(modelUsers);
            return;
        }
        usersListView.setItems(modelSearch);
        Set<String> conversationsUsers = getMatchingUsernamesFromConversations(searchValue);
        Iterable<String> friendsUsers = serviceFriendships.getAllFriendsMatchingPattern(this.username, searchValue);
        populateModelSearch(conversationsUsers, friendsUsers);
    }

    /**
     * A class with firstname, lastname and username
     */
    static class UserHBox extends AbstractConversationBox {
        private final String username;

        public UserHBox(User user) {
            nameVBox = VBoxesGenerator.createUserNameAndUsernameVBox(user);
            this.username = user.getUsername();
            this.getChildren().add(nameVBox);
        }

        public String getFriendUsername() {
            return this.username;
        }
    }
}
