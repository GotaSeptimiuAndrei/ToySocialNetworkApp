module java_projects.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens java_projects.demo to javafx.fxml;
    exports java_projects.demo;
}