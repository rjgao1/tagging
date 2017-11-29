package FrontEnd;

import Model.Config;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private Stage stage;

    @FXML
    public void initialize() throws IOException {
        // Read the config file if there is one
        if (Config.hasConfigFile()) {
            try {
                Config.readConfigFile();
            } catch (IOException e) {
                Config.deleteConfigFile();
                Config.createConfigFile();
                Config.setDefaultPath("");
            }
            directory.setText(Config.getDefaultPath());
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose root directory");
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            Config.setDefaultPath(selectedDirectory.getAbsolutePath());
            directory.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void applyButtonClicked() throws IOException {
        File directoryPath = new File(directory.getText());
        if (!directoryPath.isDirectory()) {
            popWarningMessageBox();
        } else {
            Config.setDefaultPath(directory.getText());
            if (!Config.hasConfigFile()) {
                Config.createConfigFile();
            }
            Config.writeConfigFile();
            if (Main.getMainWindowCount() == 0) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
                Stage mainWindow = loader.load();
                mainWindow.show();
                Main.incrementMainWindowCount();
            }
            stage.close();
        }
    }

    public void cancelButtonClicked() throws IOException {
        if (!Config.getDefaultPath().equals("")) {
            stage.close();
        } else {
            popWarningMessageBox();
        }
    }

    public void popWarningMessageBox() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
        Stage messageBox = loader.load();
        messageBox.setTitle("Warning");
        ((MessageBoxController) loader.getController()).setMessage("Please enter a valid directory");
        messageBox.show();
    }

}
