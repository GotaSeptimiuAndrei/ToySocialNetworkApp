package java_projects.demo.fxml;

import java_projects.demo.config.Config;
import java_projects.demo.domain.Friendship;
import java_projects.demo.domain.User;
import java_projects.demo.fxml.scene_changer.LoginRegisterSceneChanger;
import java_projects.demo.repository.FriendshipsRepository;
import java_projects.demo.repository.IRepository;
import java_projects.demo.repository.MessagesRepository;
import java_projects.demo.repository.UsersRepository;
import java_projects.demo.service.ServiceMessages;
import java_projects.demo.service.ServiceUsers;
import java_projects.demo.service.ServiceFriendships;
import java_projects.demo.service.Services;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Runner extends Application {
    private void openLoginPage(Services services) throws Exception {
        LoginRegisterSceneChanger.changeSceneToLogin(new Stage(), services);
        LoginRegisterSceneChanger.changeSceneToLogin(new Stage(), services);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        IRepository<String, User> usersRepository = new UsersRepository(
                Config.getProperties().getProperty("databaseUrl"),
                Config.getProperties().getProperty("username"),
                Config.getProperties().getProperty("password")
        );
        IRepository<ArrayList<String>, Friendship> friendshipsRepository = new FriendshipsRepository(
                Config.getProperties().getProperty("databaseUrl"),
                Config.getProperties().getProperty("username"),
                Config.getProperties().getProperty("password")
        );
        MessagesRepository messagesRepository = new MessagesRepository(
                Config.getProperties().getProperty("databaseUrl"),
                Config.getProperties().getProperty("username"),
                Config.getProperties().getProperty("password")
        );

        ServiceUsers serviceUsers = new ServiceUsers(usersRepository);
        ServiceFriendships serviceFriendships = new ServiceFriendships(friendshipsRepository);
        ServiceMessages serviceMessages = new ServiceMessages(messagesRepository);

        Services services = new Services(serviceUsers, serviceFriendships, serviceMessages);
        try {
            openLoginPage(services);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
