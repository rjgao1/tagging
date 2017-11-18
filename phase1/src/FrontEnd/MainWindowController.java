package FrontEnd;

import Model.*;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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


public class MainWindowController implements Observer {

    private FileManager fileManager;
    private Image image;

    @FXML
    ListView<String> fileList;
    @FXML
    ListView<String> tagList;
    @FXML
    TextField tagText;
    @FXML
    Stage stage;

    private ObservableList<String> files;
    private ObservableList<String> tags;

    public MainWindowController() {
        fileManager = new FileManager(Config.getDefaultPath());
    }

    @FXML
    public void initialize() {
        fileList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        loadFileList();
    }

    public void closeWindow() {
        stage.close();
        Main.decreaseMainWindowCount();
    }

    public void openMainWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Stage mainWindow = loader.load();
        mainWindow.show();
        Main.incrementMainWindowCount();
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
                if (!image.hasObserver(this)) {
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
        } else if (e.getClickCount() == 2) {
            if (file.isDirectory()) {
                fileManager = new FileManager(file.getAbsolutePath());
                loadFileList();
            }
        }
    }

    public void addTags() throws IOException {
        if (!tagText.getText().matches("@(\\w)+( @(\\w)+)*")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
            Stage messageBox = loader.load();
            messageBox.setTitle("Warning");
            ((MessageBoxController) loader.getController()).setMessage("Please enter tags with pattern @tag");
            messageBox.show();
        } else {
            String[] newTagNameList = tagText.getText().split("( )?@");
            List<String> tagNameList = tagList.getItems();
            List<Tag> newTagList = new ArrayList<>(0);
            for (String s : tagNameList) {
                newTagList.add(new Tag(s));
            }
            for (String s : newTagNameList) {
                newTagList.add(new Tag(s));
            }

            TagInfo newTagInfo = new TagInfo((Tag[]) newTagList.toArray());
            image.getLogManager().addTagInfo(newTagInfo);
        }
    }

    public void deleteTags() {
        List<String> list = tagList.getSelectionModel().getSelectedItems();
        if (tagList.getSelectionModel().getSelectedItem() != null) {
            List<String> tagNameList = tagList.getItems();
            tagNameList.removeAll(list);
            List<Tag> newTagList = new ArrayList<>(0);
            for (String s : tagNameList) {
                newTagList.add(new Tag(s));
            }

            TagInfo newTagInfo = new TagInfo((Tag[]) newTagList.toArray());
            image.getLogManager().addTagInfo(newTagInfo);
        }
    }

    public void viewHistory() throws IOException {
        String fileString = image.getFile().getPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TagHistory.fxml"));
        Stage tagHistory = loader.load();
        ((TagHistoryController) loader.getController()).setLogManager(image.getLogManager());
        tagHistory.show();
    }

    private void loadFileList() {
        ArrayList<String> list = fileManager.getImageFileNames();
        list.addAll(fileManager.getDirectoryNames());
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            s = s.substring(s.lastIndexOf(System.getProperty("file.separator")) + 1);
            if (!Config.getViewTags()) {
                if (s.contains(" @")) {
                    String postfix = s.substring(s.lastIndexOf("."));
                    s = s.substring(0, s.indexOf(" @"));
                    s = s + postfix;
                }
            }
            list.set(i, s);
        }

        files = FXCollections.observableArrayList(list);
        fileList.setItems(files);
    }

    private void loadTagList() {
        Tag[] list = image.getLogManager().getTagInfos().get(image.getLogManager().getTagInfos().size() - 1).getTagList();
        tags = FXCollections.observableArrayList();
        for (Tag tag : list) {
            tags.add(tag.getContent());
        }
        tagList.setItems(tags);
    }

    @Override
    public void update() {
        loadFileList();
        loadTagList();
    }

    public Stage getStage() {
        return stage;
    }
}
