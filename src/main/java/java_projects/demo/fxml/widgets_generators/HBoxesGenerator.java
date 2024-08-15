package java_projects.demo.fxml.widgets_generators;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class HBoxesGenerator {
    public static HBox create2StringsHBox(String firstname, String lastname) {
        HBox hbox = new HBox();
        hbox.getChildren().add(new Label(firstname + " "));
        hbox.getChildren().add(new Label(lastname));
        return hbox;
    }
}
