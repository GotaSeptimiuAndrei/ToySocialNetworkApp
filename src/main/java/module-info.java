module java_projects.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.compiler;
    requires java.desktop;
    requires javafx.swing;


    opens java_projects.demo to javafx.fxml;
    exports java_projects.demo;
}