package FrontEnd;

import Model.LogManager;
import Model.TagInfo;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;


public class TagHistoryController {

    private LogManager logManager;
    private String fileName;

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

    public void setLogManager(LogManager logManager) throws IOException {
        this.logManager = logManager;
        if (logManager.getTagInfos().size() != 0) {
            loadData();
        } else {
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
            Stage messageBox = loader.load();
            messageBox.setTitle("Warning");
            ((MessageBoxController) loader.getController()).setMessage("Image file do not has tag history");
            messageBox.show();
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void loadData() {
        data = FXCollections.observableArrayList(logManager.getTagInfos());
        historyTable.setItems(data);
        timeColumn.setCellValueFactory(new Callback<CellDataFeatures<TagInfo, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<TagInfo, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getTime());
            }
        });

        tagsColumn.setCellValueFactory(new Callback<CellDataFeatures<TagInfo, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<TagInfo, String> p) {
                String ret = fileName;
                String suffix = ret.substring(ret.lastIndexOf("."));
                ret = ret.substring(0, ret.indexOf("@"));
                ret = ret + " " + p.getValue().getTagListString() + suffix;
                return new ReadOnlyObjectWrapper(ret);
            }
        });
    }


    public Stage getStage() {
        return stage;
    }


    public void goToHistory() throws IOException {
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
