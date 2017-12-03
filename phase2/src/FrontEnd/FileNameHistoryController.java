package FrontEnd;

import Model.MasterLogManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class FileNameHistoryController {

    @FXML
    ListView<String> masterLogListView;
    @FXML
    Stage stage;

    @FXML
    public void initialize() {
        ObservableList<String> masterLogList = FXCollections.observableList(MasterLogManager.getMasterLogs());
        masterLogListView.setItems(masterLogList);
    }

    public void closeWindow() {
        stage.close();
    }

}
