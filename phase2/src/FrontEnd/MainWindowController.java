package FrontEnd;

import Model.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    @FXML
    ListView<String> tagSet;
    @FXML
    ImageView imageView;

    private ObservableList<String> files;
    private ObservableList<String> tags;
    private ObservableList<String> tagSetList;

    public MainWindowController() {
        fileManager = new FileManager(Config.getDefaultPath());
    }

    @FXML
    public void initialize() {
        fileList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        loadTagSet();
        loadFileList();
    }

    public void closeWindow() {
        if (image != null) {
            image.deleteObserver(this);
        }
        stage.close();
        Main.decreaseMainWindowCount();
    }

    public void openMainWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Stage mainWindow = loader.load();
        mainWindow.show();
        Main.incrementMainWindowCount();
    }

    public void chooseFile() throws IOException{
        if (image != null) {
            image.deleteObserver(this);
        }
        image = new Image(Config.getDefaultPath() + System.getProperty(File.separator) +
                fileList.getSelectionModel().getSelectedItem());
        Tag[] tagsFromName = Image.getTagsFromName(image.getFile().getAbsolutePath());
        if (image.getLogManager().getTagInfos().size() == 0) {
            image.getLogManager().addTagInfo(new TagInfo(tagsFromName));
        }
        for (Tag tag: tagsFromName) {
            Tag.addTagToSet(tag);
        }
        loadTagList();
    }

    //@todo
    public void addTagToSet() {

    }
    
    public void addTagToImage() throws IOException{
        if (image == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
            Stage messageBox = loader.load();
            messageBox.setTitle("Warning");
            ((MessageBoxController) loader.getController()).setMessage("Please choose an image file");
            messageBox.show();
        } else {
            List<String> tagsAdded = tagSet.getSelectionModel().getSelectedItems();
            for (String tagString: tagsAdded) {
                Tag.addTagToSet(new Tag(tagString));
            }
        }
    }

    //@todo
    public void addTags() throws IOException {
        if (image == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
            Stage messageBox = loader.load();
            messageBox.setTitle("Warning");
            ((MessageBoxController) loader.getController()).setMessage("Please choose a image file");
            messageBox.show();
            return;
        }
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
            for (int i = 1; i < newTagNameList.length; i++) {
                newTagList.add(new Tag(newTagNameList[i]));
            }

            Tag[] tagArray = new Tag[newTagList.size()];
            for (int i = 0; i < tagArray.length; i++) {
                tagArray[i] = newTagList.get(i);
            }
            TagInfo newTagInfo = new TagInfo(tagArray);
            image.getLogManager().addTagInfo(newTagInfo);
        }
    }

    //@todo
    public void deleteTags() throws IOException{
        List<String> list = tagList.getSelectionModel().getSelectedItems();
        if (tagList.getSelectionModel().getSelectedItem() != null) {
            List<String> tagNameList = tagList.getItems();
            tagNameList.removeAll(list);
            List<Tag> newTagList = new ArrayList<>(0);
            for (String s : tagNameList) {
                newTagList.add(new Tag(s));
            }

            Tag[] newTagArray = new Tag[newTagList.size()];
            for (int i = 0; i < newTagArray.length; i++) {
                newTagArray[i] = newTagList.get(i);
            }
            TagInfo newTagInfo = new TagInfo(newTagArray);
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
        files = FXCollections.observableArrayList();
        for (File file : fileManager.getImages()) {
            String path = file.getAbsolutePath();
            path = path.replace(Config.getDefaultPath(), "");
            path = path.substring(1);
            files.add(path);
        }
    }

    private void loadTagList() {
        if (image.getLogManager().getTagInfos().size() > 0) {
            Tag[] list = image.getLogManager().getTagInfos().get(image.getLogManager().getTagInfos().size() - 1).getTagList();
            tags = FXCollections.observableArrayList();
            for (Tag tag : list) {
                tags.add(tag.getContent());
            }
        } else {
            tags = FXCollections.observableArrayList();
        }
        tagList.setItems(tags);
    }

    //todo
    public void loadTagSet() {

    }

    @Override
    public void update() {
        loadFileList();
        loadTagList();
    }

    public Stage getStage() {
        return stage;
    }

    public void moveFile() throws IOException{
        if (image != null && image.getFile().isFile()) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose the new directory");
            File selectedDirectory = directoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                try {
                    String s = selectedDirectory.getAbsolutePath();
                    System.out.println(s);
                    s = s + System.getProperty("file.separator") + image.getFile().getAbsolutePath().substring(
                            image.getFile().getAbsolutePath().lastIndexOf(System.getProperty("file.separator")) + 1);
//                    Files.move(image.getFile().toPath(), Paths.get(s));
//                    fileManager = new FileManager(selectedDirectory.getAbsolutePath());
                    image.rename(s);
                    image.getLogManager().renameLogFile(s, true);
                } catch (IOException e) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
                    Stage messageBox = loader.load();
                    messageBox.setTitle("Warning");
                    ((MessageBoxController)loader.getController()).setMessage(
                            "The file with same name exists in the Directory selected"
                                    + System.getProperty("line.separator") + "Please select a new directory");
                    messageBox.show();
                }
            }

        }
    }

    public void openConfigPage() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfigPage.fxml"));
        Stage configPage = loader.load();
        configPage.show();
        configPage.setOnCloseRequest(e -> loadFileList());
    }
}
