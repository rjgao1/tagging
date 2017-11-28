package FrontEnd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import Model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainWindowController implements Observer {

    private FileManager fileManager;
    private Model.Image image;

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
    @FXML
    Text pathText;

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
        image = new Model.Image(Config.getDefaultPath() + System.getProperty("file.separator") +
                fileList.getSelectionModel().getSelectedItem());
        image.registerObserver(this);
        Tag[] tagsFromName = Model.Image.getTagsFromName(image.getFile().getAbsolutePath());
        if (image.getLogManager().getTagInfos().size() == 0 && tagsFromName.length > 0) {
            image.getLogManager().addTagInfo(new TagInfo(tagsFromName));
        }
        for (Tag tag: tagsFromName) {
            Tag.addTagToSet(tag);
        }
        loadTagList();
        pathText.setText(image.getFile().getAbsolutePath());
        imageView.setImage(new javafx.scene.image.Image(image.getFile().toURI().toString()));
    }

    public void addTagToSet() throws IOException{
        if (tagText.getText().equals("")) {
            return;
        }
        if (!tagText.getText().contains(";")) {
            if (Tag.getTagSet().contains(new Tag(tagText.getText()))) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
                Stage messageBox = loader.load();
                messageBox.setTitle("Warning");
                ((MessageBoxController) loader.getController()).setMessage("The tag is already in tag set");
                messageBox.show();
                return;
            }
            Tag.addTagToSet(new Tag(tagText.getText()));
            tagText.setText("");
            loadTagSet();
            return;
        }
        if (!tagText.getText().matches("^\\w(\\w|\\s\\w)(;\\w(\\w|\\s\\w))*")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
            Stage messageBox = loader.load();
            messageBox.setTitle("Warning");
            ((MessageBoxController) loader.getController()).setMessage("The input is not a tag or multiple tags");
            messageBox.show();
            return;
        }
        String[] tagsString = tagText.getText().split(";");
        for (String s: tagsString) {
            Tag.addTagToSet(new Tag(s));
        }
        tagText.setText("");
        loadTagSet();
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
            List<Tag> newTags = new ArrayList<>(0);
            boolean hasNewTag = false;
            for (String tagString: tags) {
                newTags.add(new Tag(tagString));
            }
            for (String tagString: tagsAdded) {
                if (!tags.contains(tagString)) {
                    newTags.add(new Tag(tagString));
                    hasNewTag = true;
                }
            }
            if (hasNewTag) {
                image.getLogManager().addTagInfo(new TagInfo(newTags.toArray(new Tag[newTags.size()])));
            }
        }
    }

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

    public void deleteTagsFromTagSet() {
        List<String> tagStringSeleted = tagSet.getSelectionModel().getSelectedItems();
        Tag[] tagSelected = new Tag[tagStringSeleted.size()];
        for (int i = 1; i < tagSelected.length; i++) {
            tagSelected[i] = new Tag(tagStringSeleted.get(i));
        }
        Tag.removeTagsFromTagSet(tagSelected);
        loadTagSet();
    }

    public void removeTags() throws IOException{
        List<String> tagsToRemove = tagSet.getSelectionModel().getSelectedItems();
        for (String s : files) {
            Model.Image temp = new Model.Image(Config.getDefaultPath() + System.getProperty(File.separator) +
                    fileList.getSelectionModel().getSelectedItem());
            List<Tag> tags = Arrays.asList(Model.Image.getTagsFromName(s));
            boolean changed = false;
            for (String tag: tagsToRemove) {
                if (s.contains("@" + tag +" ") || s.contains("@" + tag + ".")) {
                    tags.remove(new Tag(tag));
                    changed = true;
                }
            }
            if (changed) {
                temp.getLogManager().addTagInfo(new TagInfo(tags.toArray(new Tag[tags.size()])));
            }
        }
    }

    public void viewHistory() throws IOException {
        String fileString = image.getFile().getPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TagHistory.fxml"));
        Stage tagHistory = loader.load();
        ((TagHistoryController) loader.getController()).setLogManager(image.getLogManager());
        ((TagHistoryController) loader.getController()).setFileName(image.getFile().getName());
        tagHistory.show();
    }

    private void loadFileList() {
        fileManager = new FileManager(fileManager.getDirectoryAbsolutePath());
        files = FXCollections.observableArrayList();
        for (File file : fileManager.getImages()) {
            String path = file.getAbsolutePath();
            path = path.replace(Config.getDefaultPath(), "");
            path = path.substring(1);
            files.add(path);
        }
        fileList.setItems(files);
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

    public void loadTagSet() {
        tagSetList = FXCollections.observableArrayList();
        for (Tag tag: Tag.getTagSet()) {
            tagSetList.add(tag.getContent());
        }
        tagSet.setItems(tagSetList);
    }

    @Override
    public void update() {
        loadFileList();
        loadTagList();
        loadTagSet();
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
