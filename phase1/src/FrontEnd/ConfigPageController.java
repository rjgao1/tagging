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
    @FXML
    private Stage stage;

    @FXML
    public void initialize() {
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
        } else {
            viewWithTags.setSelected(true);
        }
    }

    public Stage getStage(){
        return  stage;
    }

    public void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose default directory");
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            Config.setDefaultPath(selectedDirectory.getAbsolutePath());
            directory.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void applyButtonClicked() throws IOException{
        File directoryPath = new File(directory.getText());
        if (!directoryPath.isDirectory()) {
            popWarningMessageBox();
        } else {
            Config.setDefaultPath(directory.getText());
            Config.setViewTags(viewWithTags.isSelected());
            stage.close();
            if (Main.getMainWindowCount() == 0) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
                Stage mainWindow = loader.load();
                mainWindow.show();
                Main.incrementMainWindowCount();
            }
        }
    }

    public void cancelButtonClicked() throws IOException{
        if (!Config.getDefaultPath().equals("")) {
            stage.close();
        } else {
            popWarningMessageBox();
        }
    }

    public void popWarningMessageBox() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
        Stage messageBox = loader.load();
        messageBox.setTitle("Warning");
        ((MessageBoxController)loader.getController()).setMessage("Please enter a valid directory");
        messageBox.show();
    }

}
