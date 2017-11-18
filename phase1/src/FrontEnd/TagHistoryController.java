package FrontEnd;

import Model.LogManager;
import Model.TagInfo;
import Model.Tag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;


public class TagHistoryController {

    private LogManager logManager;

    @FXML
    private TableView<TagInfo> historyTable;
    @FXML
    private TableColumn<TagInfo, String> timeColumn;
    @FXML
    private TableColumn<TagInfo, Tag[]> tagsColumn;
    @FXML
    private Button closeButton;
    @FXML
    private Stage stage;

    private ObservableList<TagInfo> data;

    @FXML
    public void initialize() {
        loadData();
    }

    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
        loadData();
    }

    public void loadData() {
        data = FXCollections.observableArrayList(logManager.getTagInfos());
        timeColumn.setCellValueFactory(new PropertyValueFactory<TagInfo, String>("time"));
        tagsColumn.setCellValueFactory(new PropertyValueFactory<TagInfo, Tag[]>("tagList"));
    }


    public Stage getStage() {
        return stage;
    }


    public void goToHistory() {
        TagInfo historyTagInfo = historyTable.getSelectionModel().getSelectedItem();
        TagInfo newTagInfo = new TagInfo(historyTagInfo.getTagList());
        logManager.addTagInfo(newTagInfo);
        this.close();
    }

    public void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
