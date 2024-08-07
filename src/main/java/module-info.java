module java_projects.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens java_projects.demo to javafx.fxml;
    exports java_projects.demo;
}