package FrontEnd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import Model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainWindowController implements Observer {

    /* The fileManager of the root directory */
    private FileManager fileManager;
    /* The image selected */
    private Model.Image image;

    @FXML
    /* The list of image files */
    ListView<String> fileList;
    @FXML
    /* The list of tags for the image */
    ListView<String> tagList;
    @FXML
    /* The textField for user to add tags */
    TextField tagText;
    @FXML
    /* The stage of this window */
    Stage stage;
    @FXML
    /* The list of independent tag set */
    ListView<String> tagSet;
    @FXML
    /* The image view */
    ImageView imageView;
    @FXML
    /* The text to show */
    Text pathText;

    /* The list of all image files */
    private ObservableList<String> files;
    /* The list of all tags for the image chose */
    private ObservableList<String> tags;

    /**
     * Initiates the MainWindowController to set root directory to fileManager.
     */
    public MainWindowController() {
        fileManager = new FileManager(Config.getDefaultPath());
    }


    /**
     * Initializes the MainWindowController to set the load the data and add tags to tag set.
     */
    @FXML
    public void initialize() throws IOException {
        pathText.setWrappingWidth(290);
        fileList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tagSet.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tagList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        for (File file : fileManager.getImages()) {
            Tag[] tagsInFile = Model.Image.getTagsFromName(file.getName());
            for (Tag tag : tagsInFile) {
                if (!Tag.getTagSet().contains(tag)) {
                    Tag.addTagToSet(tag);
                }
            }
        }
        loadTagSet();
        loadFileList();
    }

    /**
     * Closes this window.
     */
    public void closeWindow() {
        if (image != null) {
            image.deleteObserver(this);
        }
        stage.close();
        Main.decreaseMainWindowCount();
    }

    /**
     * Opens a new window.
     *
     * @throws IOException if MainWindow.fxml is moved
     */
    public void openMainWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Stage mainWindow = loader.load();
        mainWindow.show();
        Main.incrementMainWindowCount();
    }

    /**
     * Shows the image and its tags when an image is chooses.
     *
     * @throws IOException if the image file is moved
     */
    public void chooseFile() throws IOException {
        if (fileList.getSelectionModel().getSelectedItem() == null) {
            return;
        }
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
        for (Tag tag : tagsFromName) {
            Tag.addTagToSet(tag);
        }
        loadTagSet();
        loadTagList();
        pathText.setText(image.getFile().getAbsolutePath());
        imageView.setImage(new javafx.scene.image.Image(image.getFile().toURI().toString()));
    }

    /**
     * Adds tags in the textField to the tag set.
     *
     * @throws IOException if the MessageBox.fxml if moved
     */
    public void addTagToSet() throws IOException {
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
        for (String s : tagsString) {
            Tag.addTagToSet(new Tag(s));
        }
        tagText.setText("");
        loadTagSet();
    }

    /**
     * Adds tags selected in the tag set to the image.
     *
     * @throws IOException if the MessageBox.fxml is moved
     */
    public void addTagToImage() throws IOException {
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
            for (String tagString : tags) {
                newTags.add(new Tag(tagString));
            }
            for (String tagString : tagsAdded) {
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

    /**
     * Deletes tags from the image.
     *
     * @throws IOException if the image file is moved
     */
    public void deleteTags() throws IOException {
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

    /**
     * Deletes tags from the tag set.
     *
     * @throws IOException if the image file is moved
     */
    public void deleteTagsFromTagSet() throws IOException {
        List<String> tagStringSelected = tagSet.getSelectionModel().getSelectedItems();
        Tag[] tagSelected = new Tag[tagStringSelected.size()];
        for (int i = 0; i < tagSelected.length; i++) {
            tagSelected[i] = new Tag(tagStringSelected.get(i));
        }
        Tag.removeTagsFromTagSet(tagSelected);
        loadTagSet();
    }

    /**
     * Deletes tags from all the image files under the root directory.
     *
     * @throws IOException if the image file is moved
     */
    public void removeTags() throws IOException {
        ArrayList<String> tagsToRemove = new ArrayList<>(0);
        for (String s: tagSet.getSelectionModel().getSelectedItems()) {
            tagsToRemove.add(s);
        }
        if (tagsToRemove.size() == 0) {
            return;
        }
        for (String fileName : files) {
            ArrayList<Tag> tags = new ArrayList<>(0);
            for (Tag tag : Model.Image.getTagsFromName(fileName)) {
                tags.add(tag);
            }
            boolean changed = false;
            for (String tag : tagsToRemove) {
                if (fileName.contains("@" + tag + " @") || fileName.contains("@" + tag + ".")) {
                    tags.remove(new Tag(tag));
                    changed = true;
                }
            }
            if (changed) {
                Model.Image temp = new Model.Image(Config.getDefaultPath() + System.getProperty("file.separator") +
                        fileName);
                if (image != null && temp.getFile().getAbsolutePath().equals(image.getFile().getAbsolutePath())) {
                    temp = image;
                }
                temp.registerObserver(this);
                temp.getLogManager().addTagInfo(new TagInfo(tags.toArray(new Tag[tags.size()])));
                temp.deleteObserver(this);
            }
        }
    }

    /**
     * Opens the history window to view history.
     *
     * @throws IOException if TagHistory.fxml is moved
     */
    public void viewHistory() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TagHistory.fxml"));
        Stage tagHistory = loader.load();
        ((TagHistoryController) loader.getController()).setLogManager(image.getLogManager());
        ((TagHistoryController) loader.getController()).setFileName(image.getFile().getName());
        tagHistory.show();
    }

    /**
     * Adds tags to the selected directory.
     *
     * @throws IOException if the image file is moved
     */
    public void addTagsToDir() throws IOException {
        ArrayList<String> tagStringList = new ArrayList<>(0);
        for (String s: tagSet.getSelectionModel().getSelectedItems()) {
            tagStringList.add(s);
        }
        if (tagStringList.size() == 0) {
            return;
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose the directory to add tags");
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory == null) {
            return;
        }
        FileManager dir = new FileManager(selectedDirectory.getAbsolutePath());
        for (File tempImage : dir.getImages()) {
            ArrayList<Tag> tagsToAdd = new ArrayList<>(0);
            for (String s : tagStringList) {
                if (!(tempImage.getName().contains("@" + s + " @") || tempImage.getName().contains("@" + s + "."))) {
                    tagsToAdd.add(new Tag(s));
                }
            }
            if (tagsToAdd.size() != 0) {
                Model.Image temp = new Model.Image(tempImage.getAbsolutePath());
                if (image != null && temp.getFile().getAbsolutePath().equals(image.getFile().getAbsolutePath())) {
                    temp = image;
                }
                temp.registerObserver(this);
                ArrayList<Tag> tags = new ArrayList<>(0);
                for (Tag tag : Model.Image.getTagsFromName(tempImage.getName())) {
                    tags.add(tag);
                }
                tags.addAll(tagsToAdd);
                temp.getLogManager().addTagInfo(new TagInfo(tags.toArray(new Tag[tags.size()])));
                temp.deleteObserver(this);
            }
        }
    }

    /**
     * Loads the file list with the files.
     */
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

    /**
     * Loads the image tag list with its tags.
     */
    private void loadTagList() {
        if (image == null) {
            return;
        }
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

    /**
     * Loads that tag set.
     */
    private void loadTagSet() {
        ObservableList<String> tagSetList = FXCollections.observableArrayList();
        for (Tag tag : Tag.getTagSet()) {
            tagSetList.add(tag.getContent());
        }
        tagSet.setItems(tagSetList);
    }


    /**
     * Updates the data in view.
     */
    @Override
    public void update() {
        loadFileList();
        loadTagList();
        loadTagSet();
    }

    /**
     * Moves file to the new directory.
     *
     * @throws IOException if MessageBox.fxml is moved
     */
    public void moveFile() throws IOException {
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
                    ((MessageBoxController) loader.getController()).setMessage(
                            "The file with same name exists in the Directory selected"
                                    + System.getProperty("line.separator") + "Please select a new directory");
                    messageBox.show();
                }
            }

        }
    }

    /**
     * Opens the config window for user to change config.
     * 
     * @throws IOException if ConfigPage.fxml is moved
     */
    public void openConfigPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfigPage.fxml"));
        Stage configPage = loader.load();
        configPage.show();
        configPage.setOnCloseRequest(e -> loadFileList());
    }

    public void viewAllHistory() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FileNameHistory.fxml"));
        Stage fileNameHistory = loader.load();
        fileNameHistory.show();
    }
}
