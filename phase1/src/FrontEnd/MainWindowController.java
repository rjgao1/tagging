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
import java.util.List;


public class MainWindowController implements Observer{


    private Stage stage = new Stage();
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
                if (!image.hasObserver(this)){
                    image.registerObserver(this);
                }
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
            List<String> tagNameList = tagList.getItems();
            List<Tag> newTagList = new ArrayList<>(0);
            for (String s: tagNameList) {
                newTagList.add(new Tag(s));
            }
            for (String s: newTagNameList) {
                newTagList.add(new Tag(s));
            }

            TagInfo newTagInfo = new TagInfo((Tag[])newTagList.toArray());
            image.getLogManager().addTagInfo(newTagInfo);
        }
    }

    public void deleteTags() {
        List<String> list = tagList.getSelectionModel().getSelectedItems();
        if (tagList.getSelectionModel().getSelectedItem() != null) {
            List<String> tagNameList = tagList.getItems();
            tagNameList.removeAll(list);
            List<Tag> newTagList = new ArrayList<>(0);
            for (String s: tagNameList) {
                newTagList.add(new Tag(s));
            }

            TagInfo newTagInfo = new TagInfo((Tag[])newTagList.toArray());
            image.getLogManager().addTagInfo(newTagInfo);
        }
    }

    public void viewHistory() {
        String fileString = image.getFile().getPath();
        TagHistoryController tagHistoryController = new TagHistoryController(image.getLogManager(), fileString);
    }

    private void loadFileList() {
        ArrayList<String> list = fileManager.getImageFileNames();
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            s = s.substring(s.lastIndexOf(System.getProperty("file.separator") + 1));
            if (!Config.getViewTags()) {
                s = s.substring(0, s.indexOf(" @"));
            }
            list.set(i, s);
        }

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

    private void loadTagList() {
        Tag[] list = image.getLogManager().getTagInfos().get(image.getLogManager().getTagInfos().size() - 1).getTagList();
        tags = FXCollections.observableArrayList();
        for (Tag tag: list) {
            tags.add(tag.getContent());
        }
        tagList.setItems(tags);
    }

    @Override
    public void update() {
        loadFileList();
        loadTagList();
    }
}
