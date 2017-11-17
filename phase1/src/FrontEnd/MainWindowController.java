package FrontEnd;

import Model.Config;
import Model.FileManager;
import Model.Tag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainWindowController {


    private Stage stage;
    private FileManager fileManager;

    @FXML
    ListView fileList;
    @FXML
    ListView tagList;
    @FXML
    TextField tagText;

    private ObservableList<String> files;
    private ObservableList<Tag> tags;

    public MainWindowController() {
        fileManager = new FileManager(Config.getDefaultPath());

        // Load FXML File
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

        //Load the data
        ArrayList<String> list = fileManager.getImageFileNames();
        list.addAll(fileManager.getDirectoryNames());
        files = FXCollections.observableArrayList(list);
    }

    public void closeWindow() {
        stage.close();
    }

    public void openMainWindow() {

    }

    public void openDirectory() {

    }

    public void addTags() {

    }

    public void deleteTags() {

    }

    public void viewHistory() {

    }

}
