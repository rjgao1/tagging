package FrontEnd;

import Model.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainWindowController {


    private Stage stage;
    private FileManager fileManager;
    private Image image;

    @FXML
    ListView<String> fileList;
    @FXML
    ListView<String> tagList;
    @FXML
    TextField tagText;

    private ObservableList<String> files;
    private ObservableList<String> tags;

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

        loadFileList();

        fileList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        stage.show();
    }

    public void closeWindow() {
        stage.close();
    }

    public void openMainWindow() {
        MainWindowController mainWindowController = new MainWindowController();
    }

    public void openDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose the new directory");
        File selectedDirectory = directoryChooser.showDialog(stage);
        fileManager = new FileManager(selectedDirectory.getAbsolutePath());

        loadFileList();
    }

    public void chooseFile(MouseEvent e) {
        File file = new File(fileManager.getDirectoryAbsolutePath() + System.getProperty("file.separator")
                + fileList.getSelectionModel().getSelectedItem());
        if (e.getClickCount() == 1) {
            if (file.isFile()) {
                image = new Image(file.getAbsolutePath());
                //Load tagList
                Tag[] tagArray = Image.getTagsFromName(file.getAbsolutePath());
                tags = FXCollections.observableArrayList();
                for (Tag tag : tagArray) {
                    tags.add(tag.getContent());
                }
                tagList.setItems(tags);
            }
        } else if(e.getClickCount() == 2) {
            if (file.isDirectory()) {
                fileManager = new FileManager(file.getAbsolutePath());
                loadFileList();
            }
        }
    }

    public void addTags() {
        if (!tagText.getText().matches("@(\\w)+( @(\\w)+)*")) {
            MessageBoxController m = new MessageBoxController("Warning", "Please enter tags with pattern @tag");
        } else {
            String[] newTagNameList = tagText.getText().split("( )?@");
            Tag[] newTagList = new Tag[newTagNameList.length];
            for (int i = 0; i < newTagNameList.length; i++) {
                newTagList[i] = new Tag(newTagNameList[i]);
            }
            TagInfo newTagInfo = new TagInfo(newTagList);
            image.getLogManager().addTagInfo(newTagInfo);

            loadFileList();
            //Load tagList
            tags = FXCollections.observableArrayList(newTagNameList);
            tagList.setItems(tags);
        }
    }

    public void deleteTags() {
        if (tagList.getSelectionModel().getSelectedItem() == null) {

        }
    }

    public void viewHistory() {
        String fileString = image.getFile().getPath();
        TagHistoryController tagHistoryController = new TagHistoryController(image.getLogManager(), fileString);
    }

    public void loadFileList() {
        ArrayList<String> list = fileManager.getImageFileNames();
        for (String s: list) {
            s = s.substring(s.lastIndexOf(System.getProperty("file.separator") + 1));
            if (!Config.getViewTags()) {
                s = s.substring(0, s.indexOf(" @"));
            }
        }
        list.addAll(fileManager.getDirectoryNames());
        files = FXCollections.observableArrayList(list);
        fileList.setItems(files);
    }

}
