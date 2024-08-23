package java_projects.demo.fxml.scene_changer;

import java_projects.demo.config.Config;
import java_projects.demo.fxml.Runner;
import java_projects.demo.fxml.authentication.RegisterPageController;
import java_projects.demo.fxml.authentication.LoginPageController;
import java_projects.demo.service.Services;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginRegisterSceneChanger {
    /**
     * Configure the controller of the register page
     *
     * @param fxmlLoader - FXMLoader - the loader of the homepage
     */
    public static void configureRegisterPageController(Services services, FXMLLoader fxmlLoader) {
        RegisterPageController registerPageController = fxmlLoader.getController();
        registerPageController.setServices(services);
    }

    /**
     * Change current scene to register page
     *
     * @throws Exception - if the page change has failed
     */
    public static void changeSceneToRegister(Stage stage, Services services) throws Exception {
        if (Runner.class.getResource("/java_projects/demo/fxml/authentication/RegisterPage.fxml") == null)
            throw new Exception("Resource not found: RegisterPage.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("/java_projects/demo/fxml/authentication/RegisterPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        configureRegisterPageController(services, fxmlLoader);
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Configure the login page controller
     *
     * @param fxmlLoader - FXMLLoader - the loader of the login page
     */
    public static void configureLoginPageController(Services services, FXMLLoader fxmlLoader) {
        LoginPageController loginPageController = fxmlLoader.getController();
        loginPageController.setServices(services);
    }

    /**
     * Change scene to login page
     *
     * @throws Exception - if the change scene event has failed
     */
    public static void changeSceneToLogin(Stage stage, Services services) throws Exception {
        if (Runner.class.getResource("/java_projects/demo/fxml/authentication/LoginPage.fxml") == null)
            throw new Exception("Resource not found: LoginPage.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("/java_projects/demo/fxml/authentication/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        configureLoginPageController(services, fxmlLoader);
        stage.setTitle("Login");

        // Correctly load the image from the classpath
        String logoPath = Config.getProperties().getProperty("logoPath");
        stage.getIcons().add(new Image(Runner.class.getResource(logoPath).toExternalForm()));

        stage.setScene(scene);
        stage.show();
    }
}
