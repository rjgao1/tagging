package FrontEnd;

import Model.Config;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ConfigPageController {

    @FXML
    private TextField directory;
    @FXML
    private RadioButton viewWithTags;
    @FXML
    private RadioButton viewWithoutTags;


    @FXML
    public void initialize() {
        if (Config.hasConfigFile()) {
            try {
                Config.readConfigFile();
            } catch (IOException | ClassCastException e) {
                Config.deleteConfigFile();
                Config.createConfigFile();
                Config.setViewTags(true);
                Config.setDefaultPath("");
            }
            directory.setText(Config.getDefaultPath());
            if (Config.getViewTags()) {
                viewWithTags.setSelected(true);
            } else {
                viewWithoutTags.setSelected(true);
            }
        }
    }

    public void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose default directory");
        File selectedDirectory = directoryChooser.showDialog(viewWithTags.getScene().getWindow());
        Config.setDefaultPath(selectedDirectory.getAbsolutePath());
        directory.setText(selectedDirectory.getAbsolutePath());
    }

    public void applyButtonClicked() {
        File directoryPath = new File(directory.getText());
        if (!directoryPath.isDirectory()) {
            String s = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            File f = new File(s);
            f = new File(f.getParent());
            s = f.getAbsolutePath();
            s = s + System.getProperty("file.separator") + "View" + System.getProperty("file.separator") + "MessageBox.fxml";

        }
    }

    public void cancelButtonClicked() {

    }

}
