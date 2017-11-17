package FrontEnd;

import Model.Config;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private Stage stage;

    public ConfigPageController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConfigPage.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        stage.setScene(new Scene(root));

        // Read the config file if there is one
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

    public Stage getStage(){
        return  stage;
    }

    public void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose default directory");
        File selectedDirectory = directoryChooser.showDialog(stage);
        Config.setDefaultPath(selectedDirectory.getAbsolutePath());
        directory.setText(selectedDirectory.getAbsolutePath());
    }

    public void applyButtonClicked() {
        File directoryPath = new File(directory.getText());
        if (!directoryPath.isDirectory()) {
            MessageBoxController messageBox = new MessageBoxController("Warning","Directory does not exist.");
        } else {
            Config.setDefaultPath(directory.getText());
            Config.setViewTags(viewWithTags.isSelected());
            stage.close();
        }
    }

    public void cancelButtonClicked() {
        if (!Config.getDefaultPath().equals("")) {
            stage.close();
        } else {
            MessageBoxController messageBox = new MessageBoxController("Warning","Please select a default directory.");
        }
    }

}
