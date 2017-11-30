package FrontEnd;

import Model.Config;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ConfigPageController {

    @FXML
    /* The textField to show and write the directory */
    private TextField directory;
    @FXML
    /* The stage of the window */
    private Stage stage;

    /**
     * Initializes the configPage to read config file and update the textField
     */
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

    /**
     * Opens a directory chooser for user to choose directory
     */
    public void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose root directory");
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            Config.setDefaultPath(selectedDirectory.getAbsolutePath());
            directory.setText(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * Applies the change of configuration.
     * If there is no main window opening, opens a main window. Otherwise, user should restart the application
     * for new config to work.
     *
     * @throws IOException if MainWindow.fxml is moved
     */
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

    /**
     * Pops warning box if the root directory is empty, otherwise closes the config window.
     *
     * @throws IOException if MessageBox.fxml is moved
     */
    public void cancelButtonClicked() throws IOException {
        if (!Config.getDefaultPath().equals("")) {
            stage.close();
        } else {
            popWarningMessageBox();
        }
    }

    /**
     * Pops up warning box with message "Please enter a valid directory"
     *
     * @throws IOException if MessageBox.fxml is moved
     */
    private void popWarningMessageBox() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
        Stage messageBox = loader.load();
        messageBox.setTitle("Warning");
        ((MessageBoxController) loader.getController()).setMessage("Please enter a valid directory");
        messageBox.show();
    }

}
