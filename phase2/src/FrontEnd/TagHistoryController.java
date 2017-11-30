package FrontEnd;

import Model.LogManager;
import Model.TagInfo;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;


public class TagHistoryController {

    /* The logManager to get history from */
    private LogManager logManager;
    /* The name of file of which to get history */
    private String fileName;

    @FXML
    /* The table of history */
    private TableView<TagInfo> historyTable;
    @FXML
    /* The column of time */
    private TableColumn<TagInfo, String> timeColumn;
    @FXML
    /* The column of file name */
    private TableColumn<TagInfo, String> tagsColumn;
    @FXML
    /* The stage of the window */
    private Stage stage;

    /**
     * Sets the logManager.
     *
     * @param logManager the logManager to set to
     * @throws IOException if MessageBox.fxml if moved
     */
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

    /**
     * Sets file name.
     * @param fileName the file name to set to
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Loads data for history table.
     */
    private void loadData() {
        ObservableList<TagInfo> data = FXCollections.observableArrayList(logManager.getTagInfos());
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

    /**
     * Goes back to the history name.
     *
     * @throws IOException if image file is moved
     */
    public void goToHistory() throws IOException {
        TagInfo historyTagInfo = historyTable.getSelectionModel().getSelectedItem();
        TagInfo newTagInfo = new TagInfo(historyTagInfo.getTagList());
        logManager.addTagInfo(newTagInfo);
        this.close();
    }

    /**
     * Closes the window.
     */
    public void close() {
        stage.close();
    }
}
