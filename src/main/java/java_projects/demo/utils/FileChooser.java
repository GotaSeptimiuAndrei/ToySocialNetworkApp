package java_projects.demo.utils;

import javafx.stage.Stage;

import java.awt.*;
import java.io.File;

public class FileChooser {

    private Desktop desktop = Desktop.getDesktop();

    public File getFilePath(final Stage stage) {
        stage.setTitle("File Chooser Sample");

        final javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

        File file = fileChooser.showOpenDialog(stage);
        return file;
    }
}