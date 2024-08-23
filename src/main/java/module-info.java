module java_projects.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.compiler;
    requires java.desktop;
    requires javafx.swing;

    opens java_projects.demo.fxml.authentication to javafx.fxml;
    opens java_projects.demo.fxml.homepage to javafx.fxml;
    opens java_projects.demo.fxml.homepage.profile to javafx.fxml;
    opens java_projects.demo.fxml.scene_changer to javafx.fxml;


    exports java_projects.demo.fxml to javafx.graphics;
}
