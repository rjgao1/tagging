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
    private TableColumn<TagInfo, String> tagsColumn;
    @FXML
    private Button closeButton;
    @FXML
    private Stage stage;

    private ObservableList<TagInfo> data;

    public void setLogManager(LogManager logManager) throws IOException{
        this.logManager = logManager;
        if (logManager.getTagInfos().size() != 0) {
            loadData();
        } else {
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
            Stage messageBox = loader.load();
            messageBox.setTitle("Warning");
            ((MessageBoxController)loader.getController()).setMessage("Image file do not has tag history");
            messageBox.show();
        }
    }

    public void loadData() {
        data = FXCollections.observableArrayList(logManager.getTagInfos());
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        tagsColumn.setCellValueFactory(new PropertyValueFactory<>("tagListString"));
    }


    public Stage getStage() {
        return stage;
    }


    public void goToHistory() throws IOException{
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
